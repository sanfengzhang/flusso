package com.hanl.flink.ETL;


import com.hanl.flink.JobConfigContext;
import com.hanl.flink.Message;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/30
 * @desc:
 */
public class StringToMessage implements MapFunction<String, Message> {

    private static final long serialVersionUID = 1L;

    private String dataType;

    private JobConfigContext jobConfigContext;


    public StringToMessage(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        Map<String, Object> socketSource = jobConfigContext.getMap("flink.etl.job.source");
        Map<String, Object> config = (Map<String, Object>) socketSource.get("socketSource");
        this.dataType = config.get("dataType").toString();
    }

    @Override
    public Message map(String value) throws Exception {
        Message message = new Message(dataType, value);
        return message;
    }
}
