package com.hanl.flink.stability.checkpoint;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:
 */
@Data
public class TaskInfo {

    private String taskExecutionId;//这个对同一个task的Id是唯一的

    private String taskName;

    private int parallelism;

    private int subIndexTask;

    private String flinkNode;//在哪个运行节点上

}
