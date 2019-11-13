package com.hanl.flink.common.connectors;


import com.hanl.flink.JobConfigContext;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkBase;
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/8
 * @desc:
 */
public class DefaultElasticsearchSink {

    private JobConfigContext jobConfigContext;

    public static final String CONFIG_KEY_BULK_FLUSH_MAX_ACTIONS = "flushAction";

    private Map<String, String> userConfig = new HashMap<>();

    private List<InetSocketAddress> transportAddresses = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public DefaultElasticsearchSink(JobConfigContext jobConfigContext) throws Exception {
        this.jobConfigContext = jobConfigContext;
        Map<String, Object> userConfigObject = (Map<String, Object>) jobConfigContext.getMap("flink.etl.job.sink").get("esSink");
        userConfig.put("cluster.name", userConfigObject.get("clusterName").toString());
        userConfig.put(ElasticsearchSinkBase.CONFIG_KEY_BULK_FLUSH_MAX_ACTIONS, userConfigObject.get(CONFIG_KEY_BULK_FLUSH_MAX_ACTIONS).toString());
        userConfig.put(ElasticsearchSinkBase.CONFIG_KEY_BULK_FLUSH_INTERVAL_MS, "5000");
        List<String> addresses = (List<String>) userConfigObject.get("transportAddress");
        for (String addr : addresses) {
            String[] addArray = addr.split(":");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(addArray[0], Integer.parseInt(addArray[1]));
            transportAddresses.add(inetSocketAddress);
        }
    }

    @SuppressWarnings("unchecked")
    public ElasticsearchSink<Map<String, Object>> elasticsearchSink() {
        ElasticsearchSink<Map<String, Object>> elasticsearchSink = new ElasticsearchSink(userConfig, transportAddresses,
                new MapElasticsearchSinkFunction(), new DefaultESActionRequestFailureHandler());
        return elasticsearchSink;
    }

}
