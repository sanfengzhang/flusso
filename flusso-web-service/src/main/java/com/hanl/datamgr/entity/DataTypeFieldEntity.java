package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@Table(name = "tb_field")
@Data
public class DataTypeFieldEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Basic
    @Column(name = "field_name")
    private String fieldName;

    @Basic
    @Column(name = "field_type")
    private String fieldType;

    @Column(name = "field_default_value")
    private String fieldDefaultValue;//数据类型字段默认值，当数据丢失或者为空得时候可以取该值填补

    @Column(name = "field_format_check_regular")
    private String fieldFormatCheckRegular;//数据格式校验,支持正则表达式

    @Column(name = "field_trace_root")
    private String fieldTraceRoot;//主要是针对字段产生得描述，比如一些衍生字段是通过哪些字段计算出来得

    @ManyToOne
    @JoinColumn(name = "data_type_id")
    private DataTypeEntity dataType;

    @Column(name = "filed_length")
    private int fieldLength;//字段长度描述

    @Column(name = "filed_analyzer")
    private String analyzer;//分词

    @Column(name = "sample")
    private String sample;//数据结构样例

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//创建时间

    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;//更新时间
}
