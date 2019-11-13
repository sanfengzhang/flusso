package com.hanl.flink.stability.checkpoint.impl;

import com.hanl.flink.stability.checkpoint.CheckPointFaultHandler;
import com.hanl.flink.stability.checkpoint.CheckPointFaultMessage;
import com.hanl.flink.exception.CheckPointFaultException;

/**
 * @author: Hanl
 * @date :2019/11/12
 * @desc:检查故障日志分析
 */
public class DefaultCheckPointFaultHandler implements CheckPointFaultHandler {

    @Override
    public void handleFault(CheckPointFaultMessage faultMessage) throws CheckPointFaultException {
        //可以获取任务的反压信息进行查看，或者是task的数据处理速度
        String faultType = faultMessage.getType();
        if (faultType.contains("late message for now expired ")) {
             //有迟到的cp消息，那么需要获取时哪个节点发生的，并且找出是哪个任务
        }
        if (faultType.contains("Received message for an unknown checkpoin")) {
            //未知的CP
        }
        //当某个CP触发了，但是没有进行开始触发Starting checkpoint ({}) {} on task {}这个日志,可能是barrier未对齐造成
        //barrier：{}: Received barrier from channel {}.可以开启这个日志查找是哪个barrier延迟或丢失了。



    }
}
