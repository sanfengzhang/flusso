package com.hanl.flink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.nio.charset.Charset;

/**
 * 定义为CommonMessage,kafka的消息类继承
 *
 * @author hanlin01
 */
public class DefaultKafkaDeserializationSchema implements KafkaDeserializationSchema<Message> {

    private static final long serialVersionUID = 1L;

    private final SimpleStringSchema simpleStringSchema;

    public DefaultKafkaDeserializationSchema() {

        this("UTF-8");
    }

    public DefaultKafkaDeserializationSchema(String charSet) {

        simpleStringSchema = new SimpleStringSchema(Charset.forName(charSet));
    }

    public Message deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        byte[] messageKey = record.key();
        byte[] message = record.value();

        DefaultKafkaMessage result = new DefaultKafkaMessage();
        if (null != messageKey) {
            result.setKey(simpleStringSchema.deserialize(messageKey));
        }
        if (null != message) {
            result.setValue(simpleStringSchema.deserialize(message));
        }

        result.setTopic(record.topic());
        result.setType(record.topic());
        result.setPartition(record.partition());

        return result;
    }

    public boolean isEndOfStream(Message nextElement) {

        return false;
    }

    public TypeInformation<Message> getProducedType() {
        return Types.POJO(Message.class);
    }

}
