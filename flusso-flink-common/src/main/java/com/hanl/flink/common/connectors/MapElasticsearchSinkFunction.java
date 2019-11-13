package com.hanl.flink.common.connectors;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.action.index.IndexRequest;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/8
 * @desc:
 */
public class MapElasticsearchSinkFunction implements ElasticsearchSinkFunction<Map<String, Object>> {

    @Override
    public void process(Map<String, Object> stringObjectMap, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {

        IndexRequest indexRequest = new IndexRequest(stringObjectMap.get("indexName").toString(),stringObjectMap.get("indexName").toString());
        stringObjectMap.remove("indexName");
        indexRequest.source(stringObjectMap);
        requestIndexer.add(indexRequest);
    }
}
