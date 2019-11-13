package com.hanl.data.transform.command;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hanl.data.common.EnhanceCache;
import com.typesafe.config.Config;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.kitesdk.morphline.api.Command;
import org.kitesdk.morphline.api.CommandBuilder;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.api.Record;
import org.kitesdk.morphline.base.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc:
 * 这个接口主要是用于数据变换过程中，可能会根据原始字段的值，去其他系统或数据库、缓存系统中查询对应的数据
 * 进行补全等操作。
 */
public abstract class ExternalEnrichRecord extends AbstractCommand {

    private boolean cacheEnable;

    private LoadingCache<Object, Object> userCache;

    private int capacity;

    private String originalKey;

    private String addKeyName;

    private static final Logger log = LoggerFactory.getLogger(ExternalEnrichRecord.class);

    public ExternalEnrichRecord(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws Exception {

        super(builder, config, parent, child, context);
        originalKey = getConfigs().getString(config, "originalKey");
        addKeyName = getConfigs().getString(config, "addKeyName");
        cacheEnable = getConfigs().getBoolean(config, "cacheEnable");
        capacity = getConfigs().getInt(config, "capacity");
    }

    protected void initializingAfter() {
        if (cacheEnable) {
            userCache = EnhanceCache.create(capacity, new CacheLoader<Object, Object>() {
                @Nullable
                @Override
                public Object load(@NonNull Object key) throws Exception {
                    log.info("load data form cache key={}", key);
                    return loadObjectFromExternal(key);
                }
            });
            Map<Object, Object> result = initCache();
            if (null != result && !result.isEmpty()) {
                log.info("start init cache.");
                userCache.putAll(result);
            }

        }
    }


    protected abstract Object loadObjectFromExternal(Object key) throws Exception;

    protected abstract Map<Object, Object> initCache();

    @Override
    protected boolean doProcess(Record record) {

        try {

            Object queryByValue = record.getFirstValue(originalKey);
            put(record, addKeyName, cacheEnable ? userCache.get(queryByValue) : loadObjectFromExternal(queryByValue));

        } catch (Exception e) {

        }
        return super.doProcess(record);
    }

    protected void put(Record record, String key, Object value) {
        record.getFields().put(key, value);
    }
}
