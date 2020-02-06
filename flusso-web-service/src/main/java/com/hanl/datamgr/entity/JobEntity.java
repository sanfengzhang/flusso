package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@ToString(exclude = {"jobFlowRelSet"})
@EqualsAndHashCode(exclude = {"jobFlowRelSet"})
@Entity
@Table(name = "tb_job")
public class JobEntity implements Serializable {

    public static final String JOB_RUNNING = "running";

    public static final String JOB_STOP = "stopped";

    public static final String JOB_NO_DEPLOY = "NO_DEPLOY";

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_config_params")//Job级别的配置参数
    private String jobConfigParams;

    @Column(name = "job_status")
    private String jobStatus;//当前job的运行状态

    @OneToMany(targetEntity = JobDataProcessFlowRelationEntity.class, mappedBy = "jobEntity", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"jobEntity"})
    private List<JobDataProcessFlowRelationEntity> jobFlowRelSet = new ArrayList<>();

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间

}
