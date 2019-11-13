package com.hanl.flink.stability.checkpoint;

import com.hanl.flink.exception.CheckPointFaultException;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:检查故障日志分析
 */
public interface CheckPointFaultHandler {


    /**
     * 定义CP失败日志处理的类
     *
     * @param faultMessage
     * @throws CheckPointFaultException
     */
    public void handleFault(CheckPointFaultMessage faultMessage) throws CheckPointFaultException;


}
