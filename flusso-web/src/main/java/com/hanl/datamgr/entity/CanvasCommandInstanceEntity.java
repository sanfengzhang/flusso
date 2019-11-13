package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
@Entity
@Table(name = "canvas_cmd_instance")
@Data
public class CanvasCommandInstanceEntity implements Serializable {

    @Id
    private String id;


    @Column(name = "canvas_node_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "cmd_instance_id")
    private CommandInstanceEntity commandInstanceEntity;

    @Column(name = "left_px")
    private String left;

    @Column(name = "top")
    private String top;

    @Column(name = "ico")
    private String ico;

    @Column(name = "show_cmd", columnDefinition = "TINYINT(1)")
    private boolean show;

    @Column(name = "first", columnDefinition = "TINYINT(1)")
    private boolean first;//标记当前节点是否是起始节点，在画流程图计算的时候

    @Column(name = "last", columnDefinition = "TINYINT(1)")
    private boolean last;////标记当前节点是否是结束点，在画流程图计算的时候

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//任务更新时间
}
