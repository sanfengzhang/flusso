package com.hanl.datamgr;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */
public class MenuTest {

    @Test
    public void testMenu() {
        Map<String, Object> jobParams = new HashMap<>();
        jobParams.put("restartAttempts", 3);
        jobParams.put("delayBetweenAttempts", 30000);
        jobParams.put("timeCharacteristic", "EventTime");
        jobParams.put("checkpointInterval", 15000);
        jobParams.put("checkpointTimeout", 30000);
        jobParams.put("parallelism", 2);
        System.out.println(JSON.toJSONString(jobParams));

    }
}
