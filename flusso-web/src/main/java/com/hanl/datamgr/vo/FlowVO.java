package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.CanvasCommandInstanceEntity;
import com.hanl.datamgr.entity.DataProcessFlowEntity;
import com.hanl.datamgr.entity.FlowLineEntity;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Data
@ToString
public class FlowVO implements Serializable {

    private String id;

    @NotBlank(message = "流程名字不能为空")
    private String dataProcessFlowName;

    private String loadExternalLibsPath;

    private DataProcessFlowEntity flowEntity;

    private List<Map<String, String>> lineList = new ArrayList<>();

    private List<CanvasCommandInstanceEntity> nodeList = new ArrayList<>();


    public void formEntityToLineList() {
        if (null == flowEntity) {
            return;
        }
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        for (FlowLineEntity flowLineEntity : flowLineEntitySet) {
            Map<String, String> lineMap = new HashMap<>();
            String from = flowLineEntity.getStart().getId();
            String to = null;
            if (null != flowLineEntity.getEnd()) {
                to = flowLineEntity.getEnd().getId();
            }
            lineMap.put("from", from);
            lineMap.put("to", to);
            lineList.add(lineMap);
        }
    }

    public void fromEntityToNodeList() {
        Set<FlowLineEntity> flowLineEntitySet = flowEntity.getFlowLineSet();
        if (!CollectionUtils.isEmpty(flowLineEntitySet)) {
            Map<String, CanvasCommandInstanceEntity> idInstance = new HashMap<>();
            for (FlowLineEntity flowLineEntity : flowLineEntitySet) {
                String fromId = flowLineEntity.getStart().getId();
                String toId = null;
                if (null != flowLineEntity.getEnd()) {
                    toId = flowLineEntity.getEnd().getId();
                }
                if (!idInstance.containsKey(fromId)) {
                    idInstance.put(fromId, flowLineEntity.getStart());
                }
                if (null != toId && !idInstance.containsKey(toId)) {
                    idInstance.put(toId, flowLineEntity.getEnd());
                }
            }
            nodeList.addAll(idInstance.values());
        }
    }
}
