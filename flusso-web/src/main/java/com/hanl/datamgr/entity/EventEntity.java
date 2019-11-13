package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
@Data
@Entity
@Table(name = "event")
public class EventEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "event_detail")
    private String eventDetail;

    @Column(name = "event_type")
    private int eventType;

    @Column(name = "event_result")
    private String result;

    @Column(name = "event_cost_time")
    private int tookTime;//事件耗时

    @Column(name = "event_start_time")
    private Date eventStartTime;//事件开始时间

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;

}
