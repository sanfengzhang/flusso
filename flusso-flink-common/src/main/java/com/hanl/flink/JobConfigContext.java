package com.hanl.flink;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/8/29
 * @desc:
 */
public class JobConfigContext implements Serializable {

    private Map<String, Object> jobConfigParams = new HashMap<>();

    public Map<String, Object> getJobConfigParams() {
        return jobConfigParams;
    }

    public Long getLong(String key) throws Exception {
        return Long.parseLong(getString(key));
    }

    public String getString(String key) throws Exception {
        return jobConfigParams.get(key).toString();
    }

    public int getInt(String key) throws Exception {
        return Integer.parseInt(getString(key));
    }

    public Map<String, Object> getMap(String key) {

        return (Map<String, Object>) jobConfigParams.get(key);
    }
}
