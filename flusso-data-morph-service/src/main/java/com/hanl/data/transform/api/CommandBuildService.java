package com.hanl.data.transform.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/3
 * @desc:
 */
public class CommandBuildService {


    public static Map<String, Object> readLine(String charset, boolean ignoreFirstLine) {
        Map<String, Object> readLineMap = new HashMap<>();
        Map<String, Object> charsetMap = new HashMap<>();
        charsetMap.put("charset", charset);
        charsetMap.put("ignoreFirstLine", ignoreFirstLine);
        readLineMap.put("readLine", charsetMap);
        return readLineMap;
    }

    public static Map<String, Object> spilt(String inputField, List<String> outputFields, String separator, boolean isRegex, boolean addEmptyStrings, boolean trim, int limit) {
        Map<String, Object> spiltMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("inputField", inputField);
        propsMap.put("outputFields", outputFields);
        propsMap.put("separator", separator);
        propsMap.put("isRegex", isRegex);
        propsMap.put("addEmptyStrings", addEmptyStrings);
        propsMap.put("trim", trim);
        propsMap.put("limit", limit);
        spiltMap.put("split", propsMap);
        return spiltMap;
    }

    public static Map<String, Object> java(String imports, String code) {
        Map<String, Object> javaMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("imports", imports);
        propsMap.put("code", code);
        javaMap.put("java", propsMap);
        return javaMap;
    }

    public static Map<String, Object> javaMethod(String classname, String methodName, String originalKey, String deriveKey, String argumentClass) {
        Map<String, Object> javaMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("class_name", classname);
        propsMap.put("method_name", methodName);
        propsMap.put("original_key", originalKey);
        propsMap.put("derive_key", deriveKey);
        propsMap.put("argument_class", argumentClass);
        javaMap.put("javaMethodAddValue", propsMap);
        return javaMap;
    }

    public static Map<String, Object> elExpress(Map<String, String> expresses, Map<String, Object> cacheWarming) {
        Map<String, Object> elMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("expresses", expresses);
        propsMap.put("cache_warming", cacheWarming);
        elMap.put("EL", propsMap);
        return elMap;
    }

    public static Map<String, Object> jdbcEnrich(String originalKey, String addKeyName, String jdbcUrl, String driverName, String userName, String password, int capacity,
                                                 boolean cacheEnable, String querySQL, String defaultValue, String initCacheQuery) {

        Map<String, Object> jdbcMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("originalKey", originalKey);
        propsMap.put("addKeyName", addKeyName);
        propsMap.put("jdbcUrl", jdbcUrl);
        propsMap.put("driverName", driverName);
        propsMap.put("userName", userName);
        propsMap.put("password", password);
        propsMap.put("capacity", capacity);
        propsMap.put("cacheEnable", cacheEnable);
        propsMap.put("querySqlKey", querySQL);
        propsMap.put("defaultValue", defaultValue);
        propsMap.put("initCacheQuery", initCacheQuery);

        jdbcMap.put("mysqlEnrich", propsMap);
        return jdbcMap;
    }

    public static Map<String, Object> recordFieldType(Map<String, String> recordFieldType) {
        Map<String, Object> recordFieldTypeMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("fieldTypeMap", recordFieldType);
        recordFieldTypeMap.put("fieldTypeConvert", propsMap);
        return recordFieldTypeMap;
    }

    public static Map<String, Object>  callSubPipe(String pipelineSelectorKey,boolean continueParentPipe) {
        Map<String, Object> callSubPipeMap = new HashMap<>();
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put("pipelineSelectorKey", pipelineSelectorKey);
        propsMap.put("continueParentPipe", continueParentPipe);
        callSubPipeMap.put("callSubPipe", propsMap);
        return callSubPipeMap;
    }

}
