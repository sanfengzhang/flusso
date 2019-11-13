package com.hanl.datamgr.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@DiscriminatorValue("data_type_filed")
public class DataTypeFiledEntity extends FiledEntity {

    @Column(name = "filed_length",columnDefinition = "int COMMENT '字段最大长度'")
    private int fieldLength;

    @Column(name = "filed_analyzer",columnDefinition = "varchar(12) COMMENT '分词器'")
    private String analyzer;

    @Column(name = "sample", columnDefinition = "varchar(255) COMMENT '数据结构样例'")
    private String sample;
}
