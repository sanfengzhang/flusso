package com.hanl.flink.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/3
 * @desc:
 */
@Data
public class ConfigParameters implements Serializable{

    private Map<String, Object> config = new HashMap<>();


    public static ConfigParameters build() {


        return new ConfigParameters();
    }

    public void addAll(Map<String, Object> addConfig) {

        config.putAll(addConfig);
    }

    public void put(String key, Object obj) {

        config.put(key, obj);
    }

    public Object get(String key) {

        return config.get(key);
    }

    public String getString(String key) {

        return config.get(key).toString();
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    @Override
    public String toString() {
        return "ConfigParameters{" +
                "config=" + config +
                '}';
    }
}
