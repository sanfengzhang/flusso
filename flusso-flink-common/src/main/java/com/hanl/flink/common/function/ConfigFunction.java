package com.hanl.flink.common.function;


import com.hanl.flink.common.ConfigParameters;
import com.hanl.flink.Message;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
public class ConfigFunction implements MapFunction<Message, ConfigParameters> {


    @Override
    public ConfigParameters map(Message value) throws Exception {
        return null;
    }
}
