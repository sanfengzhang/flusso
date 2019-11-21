package com.hanl.datamgr.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author: Hanl
 * @date :2019/10/12
 * @desc:
 */
@Entity
@DiscriminatorValue("data_field_type_for_data_classify")
@Data
public class BusDataClassifyFieldEntity extends FieldEntity {

    @ManyToOne
    @JoinColumn(name = "bus_data_classify_id")
    private BusDataClassifyEntity busDataClassify;

    @Column(name = "filed_length", columnDefinition = "int COMMENT '字段最大长度'")
    private int fieldLength;

    @Column(name = "filed_analyzer", columnDefinition = "varchar(12) COMMENT '分词器'")
    private String analyzer;

    @Column(name = "sample", columnDefinition = "varchar(255) COMMENT '数据结构样例'")
    private String sample;


}
