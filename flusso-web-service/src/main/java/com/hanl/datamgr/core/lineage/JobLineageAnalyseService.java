package com.hanl.datamgr.core.lineage;

import com.hanl.datamgr.exception.BusException;

/**
 * @author: Hanl
 * @date :2019/11/29
 * @desc:
 * job级别的血缘关系分析，主要是基于如：SQL的方式分析（数据源表、使用的字段、计算方式、生成那些表或者插入表的数据等）
 * 2.自建ETL任务等、实时或离线任务对数据的处理过程
 */
public class JobLineageAnalyseService implements LineageAnalyseService {

    @Override
    public void handleLineage(String lineageInfo) throws BusException {

    }
}
