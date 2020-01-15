package com.hanl.datamgr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
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
        jobParams.put("test", new BigDecimal("9111111111111111111111111111111111111111111111111111111111111111111"));
        System.out.println(JSON.toJSONString(jobParams));

        String r = JSON.toJSONString(jobParams);
        Map<String, Object> map = JSON.parseObject(r, new TypeReference<Map<String, Object>>() {
        });

        System.out.println(map);

        BigInteger bigInteger = new BigInteger("91111111111111111111111111111111111");
        System.out.println(bigInteger.toString());

        System.out.println("1111111111111111111111111111111111111111".length());

    }
}
