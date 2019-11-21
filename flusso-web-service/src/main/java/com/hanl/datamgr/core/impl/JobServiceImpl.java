package com.hanl.datamgr.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hanl.datamgr.core.CommandPipeLineService;
import com.hanl.datamgr.core.JobService;
import com.hanl.datamgr.entity.DataProcessFlowEntity;
import com.hanl.datamgr.entity.JobDataProcessFlowRelationEntity;
import com.hanl.datamgr.entity.JobEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.repository.JobRepository;
import com.hanl.datamgr.vo.JobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private CommandPipeLineService commandPipeLineService;

    @Autowired
    private JobRepository jobRepository;

    @Override
    @Transactional
    public Map<String, Object> getJobConfig(String jobId) throws BusException {
        Map<String, Object> jobParams = new HashMap<>();
        Optional<JobEntity> optionalJobEntity = jobRepository.findById(jobId);
        if (!optionalJobEntity.isPresent()) {
            throw new BusException("没有找到对应的job,jobId=" + jobId);
        }
        JobEntity jobEntity = optionalJobEntity.get();

        String configString = jobEntity.getJobConfigParams();
        Map<String, Object> jobConfig = JSON.parseObject(configString, new TypeReference<Map<String, Object>>() {
        });
        jobParams.putAll(jobConfig);
        List<JobDataProcessFlowRelationEntity> relSet = jobEntity.getJobFlowRelSet();
        if (null == relSet || relSet.size() == 0) {
            throw new BusException("Job下面没有配置数据处理流程");
        }
        //FIXME 这里先只支持一个Job对应一条数据处理流程
        DataProcessFlowEntity flowEntity = relSet.get(0).getDataProcessFlowEntity();
        List<Map<String, Object>> list = commandPipeLineService.buildCommandMapConfig(flowEntity.getId());
        for (Map<String, Object> command : list) {
            //TODO 在这里只处理主流程上的source和sink节点,放在这里处理时不破换commandPipeLineService的业务完整性
            if (flowEntity.getDataProcessFlowName().equals(command.get("id"))) {
                List<Map<String, Object>> cmdList = (List<Map<String, Object>>) command.get("commands");
                if (cmdList.size() < 2) {
                    break;
                }
                Map<String, Object> source = cmdList.remove(0);
                Map<String, Object> sink = cmdList.remove(cmdList.size() - 1);
                jobParams.put("flink.etl.job.source", source);
                jobParams.put("flink.etl.job.sink", sink);
            }
        }
        jobParams.put("flink.etl.main_flow_name", flowEntity.getDataProcessFlowName());
        jobParams.put("flink.etl.morph_flow", list);
        return jobParams;
    }


    @Override
    public void crateJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void updateJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void removeJob(JobVO jobVO) throws BusException {

    }

    @Override
    public void queryJobs() throws BusException {

    }


}
