package com.hanl.flink.sample;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author: Hanl
 * @date :2019/12/10
 * @desc:
 */
public class StringToMapFunction2 implements MapFunction<String, Tuple2<String, Integer>> {

    @Override
    public Tuple2<String, Integer> map(String value) throws Exception {
        String[] tokens = value.toLowerCase().split("\\W+");
        Tuple2<String, Integer> result = new Tuple2();
        result.f0 = tokens[0];
        result.f1 = Integer.parseInt(tokens[1]);
        return result;
    }
}
