package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;
import com.hanl.data.transform.model.CommandPipeline;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/15
 * @desc:
 */
public interface CommandPipeLineService {

    /**
     * 通过数据流程获取对应流程的命令构建
     *
     * @param DataProcessFlowId
     * @return
     * @throws BusException
     */
    public List<CommandPipeline> buildCommandPipeline(String DataProcessFlowId) throws BusException;


    public List<Map<String,Object>> buildCommandMapConfig(String DataProcessFlowId) throws BusException;

}
