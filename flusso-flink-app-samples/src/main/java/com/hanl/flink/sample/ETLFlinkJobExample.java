package com.hanl.flink.sample;


import com.hanl.flink.ETL.ETLFlinkJob;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Hanl
 * @date :2019/10/29
 * @desc:
 */
@Slf4j
public class ETLFlinkJobExample extends ETLFlinkJob {

    public ETLFlinkJobExample() {
        super();
        this.initJobConfigContextByHttp("http://localhost:8080/ETL/api/v1/job/8adb929b6dcf4089016dcf40b1b70003");
    }


    public static void main(String[] args) throws Exception {
        ETLFlinkJobExample job = new ETLFlinkJobExample();
        job.run();
    }
}
