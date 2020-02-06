package com.hanl.data.transform.command;

import com.alibaba.fastjson.parser.ParserConfig;
import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.hanl.data.transform.utils.TypeUtils;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;
import org.kitesdk.morphline.base.Configs;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc: 字段类型转换，使用FastJson里面的类型转换功能，
 * 基本的数据类型功能,和JSON字符串转对象的功能
 * 或者将Map转换为JavaBean对象等功能
 */
public class FieldTypeConvertBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {

        return Collections.singletonList("fieldTypeConvert");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new RecordFieldTypeConvert(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("FieldTypeConvertBuilder init failed", config, e);
        }
    }

    @CommandDescription(morphName = "fieldTypeConvert", name = "字段类型转换节点",cmdType = "富化")
    private static final class RecordFieldTypeConvert extends AbstractCommand {

        @CommandParam(paramName = "fieldTypeMap", paramType = "java.util.Map", paramDisplayName = "字段类型映射")
        private Map<String, String> fieldTypeMap = new HashMap<>();

        @CommandParam(paramName = "parserConfig", paramType = "java.lang.String", paramDisplayName = "解析配置器")
        private ParserConfig parserConfig;

        public RecordFieldTypeConvert(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
            Config fieldTypeConfig = getConfigs().getConfig(config, "fieldTypeMap");
            for (Map.Entry<String, Object> entry : new Configs().getEntrySet(fieldTypeConfig)) {
                String type = entry.getValue().toString();
                fieldTypeMap.put(entry.getKey(), type);
            }
            this.parserConfig = (ParserConfig) context.getSettings().get("parserConfig");
            if (null == parserConfig) {
                throw new MorphlineCompilationException("FieldTypeConvertBuilder init failed because of parserConfig is null", config);
            }
            validateArguments();
        }

        @Override
        protected boolean doProcess(Record record) {

            fieldTypeMap.forEach((k, v) -> {
                Object target = TypeUtils.fastJsonCast(record.getFirstValue(k), v, parserConfig);
                record.replaceValues(k, target);
            });
            return super.doProcess(record);
        }
    }
}
