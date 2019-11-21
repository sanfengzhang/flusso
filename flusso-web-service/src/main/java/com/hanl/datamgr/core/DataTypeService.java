package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.BusDataTypeVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @author: Hanl
 * @date :2019/8/7
 * @desc:
 * 数据类型管理基本服务
 */
@Validated
public interface DataTypeService {

    /**
     * 创建数据类型
     * @param dataType
     * @throws BusException
     */

    public void createDataType(@Valid  BusDataTypeVO dataType) throws BusException;


    /**
     * 更新数据类型
     * @param dataType
     * @throws BusException
     */
    public void updateDataType(BusDataTypeVO dataType) throws BusException;


    /**
     * 删除数据类型
     * @param dataType
     * @throws BusException
     */
    public void removeDataType(BusDataTypeVO dataType)throws BusException;

}
