package com.hanl.flink.sample;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;


/**
 * @author: Hanl
 * @date :2019/11/22
 * @desc:
 */
public class StringToMapFunction implements MapFunction<String, Tuple3<Long, String, Integer>> {

    @Override
    public Tuple3<Long, String, Integer> map(String value) throws Exception {
        String[] tokens = value.toLowerCase().split("\\W+");
        Tuple3<Long, String, Integer> result = new Tuple3();
        result.f0 = Long.parseLong(tokens[0].trim());
        result.f1 = tokens[1];
        result.f2 = Integer.parseInt(tokens[2]);
        return result;
    }
}
