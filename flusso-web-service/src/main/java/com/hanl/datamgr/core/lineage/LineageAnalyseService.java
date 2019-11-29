package com.hanl.datamgr.core.lineage;

import com.hanl.datamgr.exception.BusException;

/**
 * @author: Hanl
 * @date :2019/11/29
 * @desc:
 */
public interface LineageAnalyseService {


    /**
     * 表级别血缘关系处理
     * @param lineageInfo
     * @throws BusException
     */
    public void handleLineage(String lineageInfo) throws BusException ;
}
