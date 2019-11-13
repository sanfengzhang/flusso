package com.hanl.flink.ETL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.hanl.flink.BaseFlinkJob;
import com.hanl.flink.common.ConfigParameters;
import com.hanl.flink.Constants;
import com.hanl.flink.Message;
import com.hanl.flink.common.connectors.KafkaSource;
import com.hanl.flink.common.function.ConfigFunction;
import com.hanl.flink.common.function.ConfigurableMorphlineTransformFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/4
 * @desc:
 */
@Slf4j
public class ETLConfigFlinkJob extends BaseFlinkJob {

    public ETLConfigFlinkJob() {
        log.info("*************Start ETL Config Job*******");
        this.initJobConfigContextByHttp("http://127.0.0.1:8080/ETL/api/v1/job");
    }

    @Override
    public void run() throws Exception {
        List<Map<String, Object>> morphFlows = JSON.parseObject(jobConfigContext.getString("flink.etl.morph_flow"), new TypeReference<List<Map<String, Object>>>() {
        });
        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();
        DataStream<Message> dataStream = new KafkaSource(getJobConfigContext()).getSource(env);

        DataStream<ConfigParameters> configStream = new KafkaSource(getJobConfigContext()).getSource(env).map(new ConfigFunction()).name("Message to ConfigParameters");
        BroadcastStream<ConfigParameters> broadcastConfigStream = configStream.broadcast(new MapStateDescriptor<>("config_descriptor", String.class, Map.class));

        SingleOutputStreamOperator<Map<String, Object>> etlDataStream = dataStream.connect(broadcastConfigStream).
                process(new ConfigurableMorphlineTransformFunction("Flink_ETL_Context",jobConfigContext.getString("flink.etl.main_flow_name"), morphFlows));
        etlDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();
        etlDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.UPDATE_CONFIG_PARAMETERS) {
        }).print();
        etlDataStream.print();

    }
}
