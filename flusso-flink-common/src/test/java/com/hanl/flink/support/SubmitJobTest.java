package com.hanl.flink.support;

import org.apache.flink.api.common.JobID;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.jsonplan.JsonPlanGenerator;
import org.junit.Test;

import java.io.File;

/**
 * @author: Hanl
 * @date :2019/11/11
 * @desc:
 */
public class SubmitJobTest {

    private Configuration configuration=new Configuration();

    private int parallelism = 2;

    private JobID jobId;

    @Test
    public void testPackagedProgram() throws Exception {

        final PackagedProgram packagedProgram = new PackagedProgram(new File("D:\\eclipse-workspace\\flink-dev\\data-basic-service\\flink-etl-service\\target\\flink-etl-service-1.0-SNAPSHOT.jar"),
                "com.han.stream.flink.ETL.ETLFlinkJob",
                null);
        JobGraph jobGraph= PackagedProgramUtils.createJobGraph(packagedProgram, configuration, parallelism, jobId);
        System.out.println(jobGraph.getJobID());
        System.out.println(JsonPlanGenerator.generatePlan(jobGraph));


    }

}
