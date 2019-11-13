package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString(exclude = {"jobFlowRelSet", "flowLineSet"})
@EqualsAndHashCode(exclude = {"jobFlowRelSet", "flowLineSet"})
@Entity
@Table(name = "data_process_flow")
public class DataProcessFlowEntity implements java.io.Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "data_process_flow_name")
    private String dataProcessFlowName;

    @Column(name = "load_external_lib_path")
    private String loadExternalLibsPath;//需要加载外部实现的命令插件,创建数据流程的时候会校验当前command是否都存在！

    @Column(name = "flow_status")
    private String flowStatus;//流程的状态创建、调试、待运行、运行中

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

    @Column(name = "version")
    private int version;

    @OneToMany(targetEntity = JobDataProcessFlowRelationEntity.class, mappedBy = "dataProcessFlowEntity",
            cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<JobDataProcessFlowRelationEntity> jobFlowRelSet = new HashSet<>();

    @OneToMany(mappedBy = "flowEntity", cascade = {CascadeType.PERSIST}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"flowEntity"})
    private Set<FlowLineEntity> flowLineSet = new HashSet<>();

}
