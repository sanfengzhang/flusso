package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.FlowVO;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc: 数据处理流程服务
 */

public interface DataProcessFlowService {


    public List<FlowVO> queryDataProcessFlows(String id) throws BusException;


    /**
     * 流程调试，也就是验证这个流程是否是配置期望的。
     *
     * @param id
     * @param data
     * @throws BusException
     */
    public void debugFlow(String id, String data) throws BusException;

    /**
     * 保存数据流程信息记录,可以创建或者更新     *
     * @param flowVO
     */
    public void saveDataProcessFlow(FlowVO flowVO) throws BusException;


    public void saveFlowLineRelation(FlowVO flowVO) throws BusException;

}
