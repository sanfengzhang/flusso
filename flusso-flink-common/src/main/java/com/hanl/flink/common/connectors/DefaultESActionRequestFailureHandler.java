package com.hanl.flink.common.connectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;

/**
 * @author: Hanl
 * @date :2019/11/8
 * @desc:
 */
@Slf4j
public class DefaultESActionRequestFailureHandler implements ActionRequestFailureHandler {

    @Override
    public void onFailure(ActionRequest actionRequest, Throwable failure, int i, RequestIndexer requestIndexer) throws Throwable {
        if (-1 != ExceptionUtils.indexOfThrowable(failure, EsRejectedExecutionException.class)) {
            // full queue; re-add document for indexing
            requestIndexer.add(actionRequest);
        } else if (-1 != ExceptionUtils.indexOfThrowable(failure, ElasticsearchParseException.class)) {
            // malformed document; simply drop request without failing sink
            IndexRequest indexRequest = null;
            if (actionRequest instanceof IndexRequest) {
                indexRequest = (IndexRequest) actionRequest;
            }
            log.warn("Parse document failed,actionRequest={}", indexRequest == null ? "" : indexRequest.sourceAsMap());
        } else {
            // for all other failures, fail the sink;
            // here the failure is simply rethrown, but users can also choose to throw custom exceptions
            throw failure;
        }
    }
}
