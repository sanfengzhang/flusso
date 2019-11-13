package com.hanl.flink.ETL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.hanl.flink.BaseFlinkJob;
import com.hanl.flink.Constants;
import com.hanl.flink.Message;
import com.hanl.flink.common.connectors.DefaultElasticsearchSink;
import com.hanl.flink.common.connectors.KafkaSource;
import com.hanl.flink.common.connectors.SocketSource;
import com.hanl.flink.common.function.DefaultTransformFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/29
 * @desc:
 */
@Slf4j
public abstract class ETLFlinkJob extends BaseFlinkJob {

    protected ETLFlinkJob() {
        log.info("*************Start ETL Job*******");
    }

    @Override
    public void run() throws Exception {

        List<Map<String, Object>> morphFlows = JSON.parseObject(jobConfigContext.getString("flink.etl.morph_flow"), new TypeReference<List<Map<String, Object>>>() {
        });
        StreamExecutionEnvironment env = creatStreamExecutionEnvironment();
        Map<String, Object> source = jobConfigContext.getMap("flink.etl.job.source");
        DataStream<Message> dataStreamSourceMessage = null;
        if (source.containsKey("socketSource")) {
            dataStreamSourceMessage = new SocketSource(jobConfigContext).getSource(env);
        } else if (source.containsKey("kafkaSource")) {
            dataStreamSourceMessage = new KafkaSource(jobConfigContext).getSource(env);
        }
        SingleOutputStreamOperator<Map<String, Object>> mapDataStream = dataStreamSourceMessage.
                process(new DefaultTransformFunction("Flink_Transform_Context", jobConfigContext.getString("flink.etl.main_flow_name"), morphFlows)).name("ETL-Transform");
        mapDataStream.getSideOutput(new OutputTag<Map<String, Object>>(Constants.FLINK_FAILED) {
        }).print();


        mapDataStream.addSink(new DefaultElasticsearchSink(jobConfigContext).elasticsearchSink()).name("ETL-ES-SINK");
        env.execute();
    }
}
