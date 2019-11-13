package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.JobEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString
public class JobVO extends BaseVO<JobEntity> {

    private String id;

    private String jobName;

    private Map<String, Object> jobConfig;//Job级别的配置参数

    private Date createTime;//任务创建时间

    private Date updateTime;//任务更新时间

    @Override
    public JobEntity to() {
        return null;
    }

    @Override
    public void from(JobEntity jobEntity) {

    }
}
