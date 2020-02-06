package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.kitesdk.morphline.base.Configs;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/5
 * @desc: 主要是EL表达式定义规则对数据进行计算、支持一些复杂的规则计算
 * 数据转换操作的抽象分以下几种情况：
 * 1.根据原始值通过动态方式（通过写代码脚本、Java方法调用或者直接赋值）计算增加新值--->这个是代码写好的，重新定义规则需要重启
 * 2.根据原始值通过静态方式赋值，例如脏数据、缺失数据时候直接赋予给定值--->希望能不用重启系统、动态更新一些简单的操作计算
 * 3.过滤规则，根据一些计算方式进行过滤.这个功能没有划分到transform模块中，transform主要是数据变换、不涉及数据的删除、过滤
 */
public class ELExpressBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("EL");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new ELExpressBuilder.ELExpress(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("ELExpressBuilder", config, new Throwable(e));
        }
    }

    @CommandDescription(morphName = "EL", name = "EL表达式",cmdType = "计算")
    private static final class ELExpress extends AbstractCommand {

        private ExpressRunner runner;

        @CommandParam(paramName = "cache_warming", paramType = "java.util.Map", paramDisplayName = "EL预热表达式")
        private final Map<String, String> expressesToKey = new HashMap<>();

        @CommandParam(paramName = "expresses", paramType = "java.util.Map", paramDisplayName = "EL表达式")
        private final Map<String, String> expresses = new HashMap<>();

        public ELExpress(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
            Config expressConfig = getConfigs().getConfig(config, "expresses");
            Config cacheWarming = getConfigs().getConfig(config, "cache_warming");
            for (Map.Entry<String, Object> entry : new Configs().getEntrySet(expressConfig)) {
                String arr[] = entry.getValue().toString().split(",");
                expresses.put(entry.getKey(), arr[0]);
                if (arr.length == 2) {
                    expressesToKey.put(entry.getKey(), arr[1]);
                }
            }
            validateArguments();
            runner = new ExpressRunner();
            if (!cacheWarming.isEmpty()) {
                DefaultContext defaultContext = new DefaultContext();
                new Configs().getEntrySet(cacheWarming).forEach(k -> {
                    defaultContext.put(k.getKey(), k.getValue());
                });
                expresses.forEach((k, v) -> {
                    try {
                        //EL表达式必须缓存起来，否则性能下降贼多,EL预热操作
                        LOG.info("start EL={} cache warming,The sample data={}", k, defaultContext.toString());
                        runner.execute(k, defaultContext, null, true, false);
                    } catch (Exception e) {
                        throw new MorphlineCompilationException("ELExpressBuilder", getConfig(), new Throwable(e));
                    }
                });
            }
        }

        @Override
        protected boolean doProcess(Record record) {
            handleRecordByExpress(record);
            return super.doProcess(record);
        }

        protected void put(Record record, String key, Object value) {
            Set<Object> set = new HashSet<>();
            set.add(value);
            record.getFields().replaceValues(key, set);
        }

        private void handleRecordByExpress(final Record record) {
            DefaultContext<String, Object> context = new DefaultContext<>();
            record.getFields().asMap().forEach((k, v) -> {
                Iterator<Object> it = v.iterator();
                if (it.hasNext()) {
                    context.put(k, it.next());
                } else {
                    context.put(k, null);
                }
            });
            expresses.forEach((k, v) -> {
                try {
                    //EL表达式必须缓存起来，否则性能下降贼多
                    Object result = runner.execute(k, context, null, true, false);
                    if (record != null) {
                        put(record, expresses.get(k), result);
                    }
                } catch (Exception e) {
                    throw new MorphlineCompilationException("ELExpressBuilder", getConfig(), new Throwable(e));
                }
            });
        }
    }
}
