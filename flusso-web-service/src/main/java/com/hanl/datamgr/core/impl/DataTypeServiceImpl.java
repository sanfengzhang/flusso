package com.hanl.datamgr.core.impl;

import com.hanl.datamgr.core.DataTypeService;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.BusDataTypeVO;
import org.springframework.stereotype.Service;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Service
public class DataTypeServiceImpl implements DataTypeService {

    @Override
    public void createDataType(BusDataTypeVO dataType) throws BusException {
        System.out.println("sucess。。。。");
    }

    @Override
    public void updateDataType(BusDataTypeVO dataType) throws BusException {

    }

    @Override
    public void removeDataType(BusDataTypeVO dataType) throws BusException {

    }
}
