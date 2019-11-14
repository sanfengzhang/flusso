package com.hanl.flink.common.connectors;


import com.hanl.flink.DefaultKafkaDeserializationSchema;
import com.hanl.flink.JobConfigContext;
import com.hanl.flink.Message;
import lombok.Data;
import org.apache.flink.api.common.io.ratelimiting.FlinkConnectorRateLimiter;
import org.apache.flink.api.common.io.ratelimiting.GuavaFlinkConnectorRateLimiter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
@Data
public class KafkaSource {

    //这个时flink里面会自动发现Kafka分区数量的变化,多久查询一次,ms
    public static final String KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS = "flink.partition-discovery.interval-millis";

    //还可以控制Kafka的偏移量提交的三种方式

    public static final String KEY_TOPICS = "topics";

    public static final String KEY_PATTERN = "pattern";

    public static final String KEY_KAFKA_SOURCE = "kafkaSource";

    public static final String KEY_CHARSET = "charset";

    private List<String> topics;

    private Properties props;

    private Pattern pattern;

    private String charSet;

    /**
     * Rate in bytes per second for the consumer on a whole.
     */
    private long globalRateBytesPerSecond = Long.MIN_VALUE;

    private Map<String, Object> kafkaSource;

    private JobConfigContext jobConfigContext;


    @SuppressWarnings("unchecked")
    public KafkaSource(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        this.kafkaSource = jobConfigContext.getMap("flink.etl.job.source");
        kafkaSource = (Map<String, Object>) kafkaSource.get(KEY_KAFKA_SOURCE);
        this.props = new Properties();
        this.props.putAll(kafkaSource);
        this.charSet = kafkaSource.get(KEY_CHARSET).toString();
        this.globalRateBytesPerSecond = Long.parseLong(kafkaSource.get(KEY_CHARSET) == null ?
                                          Long.MIN_VALUE + "" : kafkaSource.get(KEY_CHARSET).toString());
        this.topics = (List<String>) kafkaSource.get(KEY_TOPICS);
    }

    public DataStream<Message> getSource(StreamExecutionEnvironment env) {
        DataStream<Message> dataStreamSource;
        FlinkConnectorRateLimiter rateLimiter = null;
        if (globalRateBytesPerSecond != Long.MIN_VALUE) {
            rateLimiter = new GuavaFlinkConnectorRateLimiter();
            rateLimiter.setRate(globalRateBytesPerSecond);
        }
        FlinkKafkaConsumer010<Message> flinkKafkaConsumer010 = null;
        if (null == pattern) {
            flinkKafkaConsumer010 = new FlinkKafkaConsumer010<Message>(topics, new DefaultKafkaDeserializationSchema(charSet), props);

        } else {
            flinkKafkaConsumer010 = new FlinkKafkaConsumer010<Message>(pattern, new DefaultKafkaDeserializationSchema(charSet), props);
        }
        if (null != rateLimiter) {
            flinkKafkaConsumer010.setRateLimiter(new GuavaFlinkConnectorRateLimiter());
        }
        dataStreamSource = env.addSource(flinkKafkaConsumer010, "FLINK-KAFKA-SOURCE");

        return dataStreamSource;
    }
}
