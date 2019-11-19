package com.hanl.datamgr.entity;

import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/11/15
 * @desc:
 */
@Data
public class DataStorageEntity implements Serializable {

    private String id;

    private String storageType;//存储类型

    @ManyToOne
    @JoinColumn(name = "data_type_id")
    private BusDataClassifyEntity dataType;

}
