package com.hanl.data.transform.command;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.*;
import org.kitesdk.morphline.base.AbstractCommand;

import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

/**
 * @author: Hanl
 * @date :2019/8/3
 * @desc:
 */
public final class JavaMethodAddValueBuilder implements CommandBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("javaMethodAddValue");
    }

    @Override
    public Command build(Config config, Command parent, Command child, MorphlineContext context) {
        try {
            return new JavaMethodAddValueBuilder.DynamicAddValue(this, config, parent, child, context);
        } catch (Exception e) {
            throw new MorphlineCompilationException("Cannot compile script", config, e);
        }

    }

    @CommandDescription(morphName = "javaMethodAddValue", name = "JAVA方法富化节点",cmdType = "富化")
    private static final class DynamicAddValue extends AbstractCommand {

        private Method targetMethod;

        private Object target;

        @CommandParam(paramName = "original_key", paramType = "java.lang.String", paramDisplayName = "富化字段名称")
        private String original_key;

        @CommandParam(paramName = "derive_key", paramType = "java.lang.String", paramDisplayName = "富化后字段名称")
        private String derive_key;

        @CommandParam(paramName = "class_name", paramType = "java.lang.String", paramDisplayName = "类名称")
        private String className;

        @CommandParam(paramName = "method_name", paramType = "java.lang.String", paramDisplayName = "方法名称")
        private String methodName;

        @CommandParam(paramName = "argument_class", paramType = "java.lang.String", paramDisplayName = "参数类型")
        private Class[] argumentClazzs;

        public DynamicAddValue(CommandBuilder builder, Config config, Command parent, Command child, MorphlineContext context) throws ScriptException, ClassNotFoundException,
                InstantiationException, IllegalAccessException, NoSuchMethodException {

            super(builder, config, parent, child, context);
            className = getConfigs().getString(config, "class_name");
            methodName = getConfigs().getString(config, "method_name");
            original_key = getConfigs().getString(config, "original_key");
            derive_key = getConfigs().getString(config, "derive_key");
            String argumentClassNames[] = getConfigs().getString(config, "argument_class").split(",");

            int len = argumentClassNames.length;
            argumentClazzs = new Class[len];
            for (int i = 0; i < len; i++) {
                argumentClazzs[i] = Class.forName(argumentClassNames[i]);
            }
            validateArguments();
            Class clazz = Class.forName(className);
            target = clazz.newInstance();
            targetMethod = clazz.getMethod(methodName, argumentClazzs);
        }

        @Override
        protected boolean doProcess(Record record) {
            Object resultValue = null;
            try {
                resultValue = targetMethod.invoke(target, new Object[]{record.getFields().get(original_key).get(0)});
            } catch (IllegalAccessException | InvocationTargetException e) {

            }
            put(record, derive_key, resultValue);
            return super.doProcess(record);
        }

        protected void put(Record record, String key, Object value) {
            record.getFields().put(key, value);
        }
    }
}


