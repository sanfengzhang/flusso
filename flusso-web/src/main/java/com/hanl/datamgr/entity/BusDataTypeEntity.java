package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
@Entity
@Table(name = "data_type")
public class BusDataTypeEntity implements java.io.Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "data_type_name")
    private String dataTypeName;//数据类型名称、和业务关联

    @Column(name = "data_schema")
    private String dataSchema;//描述数据结构的schema

    @Column(name = "data_type_lineage")
    private String dataTypeLineage;//描述该类型数据的生成处理过程，可能有多种方式

    @Column(name = "data_storage")
    private String dataStorageDesc;//该数据类型持久化策略描述

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据类型创建时间

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BusDataTypeEntity sonEntity;

    @Column(name = "update_time")
    private Date updateTime;//数据类型更新时间

    @Column(name = "description")
    private String desc;
}
