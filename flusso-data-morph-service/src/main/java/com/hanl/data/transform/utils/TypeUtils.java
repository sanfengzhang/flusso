package com.hanl.data.transform.utils;

import com.alibaba.fastjson.parser.ParserConfig;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Hanl
 * @date :2019/8/8
 * @desc:
 */
public class TypeUtils {

    public static final String STRING = "java.lang.String";

    public static final String INT = "java.lang.Integer";

    public static final String FLOAT = "java.lang.Float";

    public static final String DOUBLE = "java.lang.Double";

    public static final String LONG = "java.lang.Long";

    public static final String SHORT = "java.lang.Short";

    public static final String DATE = "java.util.Date";

    public static Map<String, Class<?>> TYPE_FOR_CLAZZ = new ConcurrentHashMap<>();

    static {
        TYPE_FOR_CLAZZ.put(STRING, String.class);
        TYPE_FOR_CLAZZ.put(INT, Integer.class);
        TYPE_FOR_CLAZZ.put(FLOAT, Float.class);
        TYPE_FOR_CLAZZ.put(DOUBLE, Double.class);
        TYPE_FOR_CLAZZ.put(LONG, Long.class);
        TYPE_FOR_CLAZZ.put(SHORT, Short.class);
        TYPE_FOR_CLAZZ.put(DATE, Date.class);

    }

    public static Object fastJsonCast(Object value, String clazzName, ParserConfig parserConfig) {
        Class<?> clazz = TYPE_FOR_CLAZZ.get(clazzName);
        if (null == clazz) {
            try {
                clazz = Class.forName(clazzName);
                TYPE_FOR_CLAZZ.put(clazzName, clazz);
            } catch (ClassNotFoundException e) {
                throw new NullPointerException("Class not found class name=" + clazzName);
            }
        }
        Object result = com.alibaba.fastjson.util.TypeUtils.cast(value, clazz, parserConfig);
        return result;
    }
}
