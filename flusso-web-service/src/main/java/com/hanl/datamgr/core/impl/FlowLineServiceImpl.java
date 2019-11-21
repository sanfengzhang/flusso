package com.hanl.datamgr.core.impl;

import com.hanl.datamgr.core.FlowLineService;
import com.hanl.datamgr.entity.CanvasCommandInstanceEntity;
import com.hanl.datamgr.entity.FlowLineEntity;
import com.hanl.datamgr.repository.FlowLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */
@Service
public class FlowLineServiceImpl implements FlowLineService {

    @Autowired
    private FlowLineRepository flowLineRepository;

    @Override
    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(String flowId) {
        List<FlowLineEntity> flowLineEntities = flowLineRepository.findAllByFlowEntity_Id(flowId);
        Map<String, CanvasCommandInstanceEntity> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(flowLineEntities)) {
            for (FlowLineEntity flowLineEntity : flowLineEntities) {
                CanvasCommandInstanceEntity start = flowLineEntity.getStart();
                CanvasCommandInstanceEntity end = flowLineEntity.getEnd();
                if (start.isFirst()) {
                    result.put(START_CMD, start);
                }
                if (null != end) {
                    if (end.isLast()) {
                        result.put(END_CMD, end);
                    }
                } else {
                    result.put(END_CMD, null);
                }

            }

        }
        return result;
    }

    /**
     * FIXME 采用新的方式去计算流程中的起始和结束节点,主要因为流程图的构建方式改变了，以前是因为分支和子流程想通过
     * 直接从绘图反应到程序处理的方式下，但是发现程序实现比较麻烦，需要实现算法去识别子节点和分支节点等特殊节点；因为实现
     * 过于复杂，现在将分支和子流程通过节点、单一的配置方式去实现。这样对于任意单一的流程在表示上都是一个单一的list就可以
     * 实现，所以也不需要标记某个节点是起始节点还是结束节点，只需要判断这个节点在flowLine中只在start或end中出现过一次就可以
     * 判断出是起始节点还是结束节点。     *
     * @param flowLineEntitySet
     * @return
     */
    @Override
    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(Set<FlowLineEntity> flowLineEntitySet) {
        Map<String, CanvasCommandInstanceEntity> result = new HashMap<>();
        if (CollectionUtils.isEmpty(flowLineEntitySet)) {
            return result;
        }
        List<CanvasCommandInstanceEntity> startSet = new ArrayList<>();
        List<CanvasCommandInstanceEntity> endSet = new ArrayList<>();
        for (FlowLineEntity flowLine : flowLineEntitySet) {
            startSet.add(flowLine.getStart());
            if (null != flowLine.getEnd()) {
                endSet.add(flowLine.getEnd());
            }
        }
        if (startSet.size() == 1) {
            result.put(START_CMD, startSet.get(0));
        } else {
            for (CanvasCommandInstanceEntity start : startSet) {
                if (!endSet.contains(start)) {
                    result.put(START_CMD, start);
                }
            }
        }
        if (endSet.size() == 0) {
            result.put(END_CMD, null);
        } else {
            for (CanvasCommandInstanceEntity end : endSet) {
                if (!startSet.contains(end)) {
                    result.put(END_CMD, end);
                }
            }
        }
        return result;
    }

}
