package com.hanl.flink.common.connectors;


import com.hanl.flink.ETL.StringToMessage;
import com.hanl.flink.JobConfigContext;
import com.hanl.flink.Message;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
@Data
public class SocketSource {

    private String host;

    private int port;

    private String dataType;

    private Map<String, Object> socketSource;

    private JobConfigContext jobConfigContext;

    @SuppressWarnings("unchecked")
    public SocketSource(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        this.socketSource = jobConfigContext.getMap("flink.etl.job.source");
        Map<String, Object> config = (Map<String, Object>) socketSource.get("socketSource");
        this.host = config.get("host").toString();
        this.port = Integer.parseInt(config.get("port").toString());
        this.dataType = config.get("dataType").toString();
    }

    public DataStream<Message> getSource(StreamExecutionEnvironment env) throws Exception {
        DataStream<String> dataStreamSourceString = env.socketTextStream(host, port).name("SOCKET-SOURCE");
        DataStream<Message> dataStreamSourceMessage = dataStreamSourceString.map(new StringToMessage(jobConfigContext)).name("ETL-String-to-Message");
        return dataStreamSourceMessage;
    }
}
