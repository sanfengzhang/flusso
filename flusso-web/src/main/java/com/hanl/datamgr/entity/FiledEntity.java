package com.hanl.datamgr.entity;

import lombok.Data;
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
 * @date :2019/8/7
 * @desc:
 */
@Data
@Entity
@Table(name = "filed_type")
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("filed_entity")
public class FiledEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    protected String id;

    @Basic
    @Column(name = "filed_name")
    protected String fieldName;

    @Basic
    @Column(name = "filed_type")
    protected String fieldType;

    @Column(name = "filed_value",columnDefinition = "varchar(255) COMMENT '字段赋值'")
    protected String fieldValue;

    @Column(name = "filed_format",columnDefinition = "varchar(32) COMMENT '数据格式化,yyyy-MM-dd'")
    protected String format;

    @Column(name = "cmd_display_name")
    protected String cmdDisplayName;

    @Column(name = "create_time")
    @CreationTimestamp
    protected Date createTime;//任务创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    protected Date updateTime;//任务更新时间

}
