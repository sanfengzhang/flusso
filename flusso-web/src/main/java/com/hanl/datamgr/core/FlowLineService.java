package com.hanl.datamgr.core;

import com.hanl.datamgr.entity.CanvasCommandInstanceEntity;
import com.hanl.datamgr.entity.FlowLineEntity;

import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc:
 */
public interface FlowLineService {

    public static final String START_CMD = "from";

    public static final String END_CMD = "to";

    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(String flowId);

    public Map<String, CanvasCommandInstanceEntity> findStartAndEndCmd(Set<FlowLineEntity> flowLineEntitySet);
}
