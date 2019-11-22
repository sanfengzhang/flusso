package com.hanl.datamgr.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Hanl
 * @date :2019/11/15
 * @desc: 我们是否可以这样设计存储：
 * 对数据的存储都需要进行注册认证之后，才能进行操作，否则是禁止对数据
 * 库或存储进行任何操作的。那么就需要对数据访问的权限认证功能，统一的注册中心
 * 和访问API这样可以在API中加入一个数据操作拦截，主要是针对查询。
 */
@Data
@Entity
@Table(name = "data_storage")
public class DataStorageEntity implements Serializable {

    @Id
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @Column(name = "storage_type")
    private String storageType;//存储类型

    @Column(name = "writer")
    private String write;//有哪些应用会向该表写入数据

    @Column(name = "reader")
    private String reader;//有哪些应用或者操作从这个表读取数据

    @ManyToOne
    @JoinColumn(name = "data_classify_id")
    private BusDataClassifyEntity busDataClassifyEntity;


    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime;//数据类型创建时间

    @Column(name = "update_time")
    private Date updateTime;//数据类型更新时间

}
