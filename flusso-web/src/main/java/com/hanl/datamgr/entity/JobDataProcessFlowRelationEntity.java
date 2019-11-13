package com.hanl.datamgr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@ToString(exclude = {"dataProcessFlowEntity"})
@EqualsAndHashCode(exclude = {"dataProcessFlowEntity"})
@Entity
@Table(name = "job_data_process_flow_relation")
public class JobDataProcessFlowRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;


    @ManyToOne(targetEntity = JobEntity.class)
    @JoinColumn(name = "job_id")
    @JsonIgnoreProperties(value = {"jobFlowRelSet"})
    private JobEntity jobEntity;

    @ManyToOne(targetEntity = DataProcessFlowEntity.class)
    @JoinColumn(name = "data_process_flow_id")
    @JsonIgnoreProperties(value = {"jobFlowRelSet"})
    private DataProcessFlowEntity dataProcessFlowEntity;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;
}
