package com.hanl.datamgr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */
@Data
@ToString(exclude = {"commandInstanceEntity", "flowEntity"})
@EqualsAndHashCode(exclude = {"commandInstanceEntity", "flowEntity"})
@Entity
@Table(name = "tb_cmd_instance_flow_rel")
public class CommandInstanceFlowRelation {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "cmd_instance_id")
    private CommandInstanceEntity commandInstanceEntity;

    @ManyToOne
    @JoinColumn(name = "flow_id")
    private DataProcessFlowEntity flowEntity;

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间


}
