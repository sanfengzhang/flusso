package com.hanl.datamgr.core.lineage;

import com.hanl.datamgr.entity.WideTableEntity;
import com.hanl.datamgr.exception.BusException;

/**
 * @author: Hanl
 * @date :2019/11/29
 * @desc:
 * 表注册接口
 */
public interface WideTableService {

    /**
     * 广泛表注册信息，所有的广泛表都应该需要调用该接口进行表注册
     * @param wideTableEntity
     * @throws BusException
     */
    public void registerTable(WideTableEntity wideTableEntity)throws BusException;
}
