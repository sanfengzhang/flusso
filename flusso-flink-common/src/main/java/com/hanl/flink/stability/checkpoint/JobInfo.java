package com.hanl.flink.stability.checkpoint;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:
 */
@Data
public class JobInfo {

    private String jobId;

    private String jobName;

    //按照task的名称映射所有的详细信息
    private Map<String, List<TaskInfo>> taskNameMappedDetail;
}
