package com.hanl.flink.support;

import org.apache.flink.runtime.executiongraph.ArchivedExecutionVertex;
import org.apache.flink.runtime.rest.handler.HandlerRequest;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.JobIDPathParameter;
import org.apache.flink.runtime.rest.messages.JobVertexIdPathParameter;
import org.apache.flink.runtime.rest.messages.job.SubtaskExecutionAttemptDetailsInfo;
import org.apache.flink.runtime.rest.messages.job.SubtaskMessageParameters;
import org.apache.flink.runtime.util.EvictingBoundedList;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:
 */
public class SubtaskCurrentAttemptDetailsHandlerTest {

    @Test
    public void tesSubTaskCurrentDetail() throws Exception {
        final HashMap<String, String> receivedPathParameters = new HashMap<>(2);
        receivedPathParameters.put(JobIDPathParameter.KEY, "");
        receivedPathParameters.put(JobVertexIdPathParameter.KEY, "");


    }
}
