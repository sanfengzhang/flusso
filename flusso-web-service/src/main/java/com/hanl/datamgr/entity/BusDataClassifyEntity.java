package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 */
@Data
@Entity
@Table(name = "data_type")
public class BusDataClassifyEntity implements java.io.Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "data_type_name")
    private String dataTypeName;//数据类型名称、和业务关联

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "busDataClassify")
    private Set<BusDataClassifyFieldEntity> busDataClassifyFieldSet;//描述该类型数据的字段信息

    @OneToMany(mappedBy = "busDataClassifyEntity", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<DataStorageEntity> dataStorageSet = new HashSet<>();

    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据类型创建时间

    @Column(name = "update_time")
    private Date updateTime;//数据类型更新时间

    @Column(name = "description")
    private String remark;
}
