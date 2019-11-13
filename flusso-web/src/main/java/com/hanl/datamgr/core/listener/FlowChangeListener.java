package com.hanl.datamgr.core.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hanl.datamgr.common.EventListener;
import com.hanl.datamgr.entity.DataProcessFlowEntity;
import com.hanl.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.hanl.datamgr.entity.JobEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.exception.EventException;
import com.hanl.datamgr.repository.DataProcessFlowRepository;
import com.hanl.datamgr.vo.EventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc: 1.流程配置变化，比如调整顺序、参数变化等需要重新初始化command配置，不用重启Job
 * 2.流程中的一些第三方缓存参数变化,例如静态参数表加载或刷新，不需要重启Job也不需要对command操作
 */
@Component
public class FlowChangeListener implements EventListener {

    @Autowired
    private DataProcessFlowRepository flowRepository;

    @Override
    public EventVO listen(EventVO event) throws EventException {

        if (EventStatus.CommandChange == event.getEventType()) {

        }

        return null;
    }

    private void flowChange(EventVO event) throws BusException {
        String input = event.getInput().toString();
        Map<String, String> params = JSON.parseObject(input, new TypeReference<Map<String, String>>() {
        });
        String flowId = params.get("flowId");
        DataProcessFlowEntity entity = flowRepository.findById(flowId).get();
        if (null != entity) {
            Set<JobDataProcessFlowRelationEntity> jobFlowRelations = entity.getJobFlowRelSet();
            for (JobDataProcessFlowRelationEntity jobFlowRel : jobFlowRelations) {
                JobEntity jobEntity = jobFlowRel.getJobEntity();
                if (!JobEntity.JOB_NO_DEPLOY.equals(jobEntity.getJobStatus())) {//只有Job未部署状态才不需要响应事件


                }
            }
        }
    }
}
