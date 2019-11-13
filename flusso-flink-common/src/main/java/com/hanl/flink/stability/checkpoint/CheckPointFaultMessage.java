package com.hanl.flink.stability.checkpoint;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:
 */
@Data
public class CheckPointFaultMessage {

    //Received late message for now expired checkpoint attempt {} from task {} of job {} at {}.
    private String faultMsg;//原始的错误日志：eg: Received message for an unknown checkpoint {} from task {} of job {} at {}.

    private String type;//按照checkpoint的异常进行划分类型

    private String taskExecutionId;

    private String jobId;

    private Object faultResult;//分析的结果

}
