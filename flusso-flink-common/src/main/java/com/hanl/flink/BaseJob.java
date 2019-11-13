package com.hanl.flink;


/**
 * @author: Hanl
 * @date :2019/10/28
 * @desc:
 */
public interface BaseJob {

    public void run() throws Exception;

    public JobConfigContext getJobConfigContext();

}
