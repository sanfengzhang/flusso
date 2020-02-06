package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.DataTypeEntity;
import com.hanl.datamgr.support.AlreadyInDB;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class BusDataTypeVO extends BaseVO<DataTypeEntity> {

    private String id;

    @AlreadyInDB(sql="select count(0) from data_type where data_type_name=?")
    @NotBlank(message = "数据类型名称不能为空")
    @Length(max = 255)
    private String dataTypeName;

    private String dataTypeLineage;

    private String dataStorageDesc;

    private Date createTime;

    private Date updateTime;

    private String desc;

    @Override
    public DataTypeEntity to() {

        return null;
    }

    @Override
    public void from(DataTypeEntity dataTypeEntity) {

    }
}
