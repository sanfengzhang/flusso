package com.hanl.datamgr.core.impl;

import com.hanl.datamgr.core.CommandInstanceService;
import com.hanl.datamgr.entity.*;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.repository.*;
import com.hanl.datamgr.vo.CommandInstanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Service
@Slf4j
public class CommandInstanceServiceImp implements CommandInstanceService {

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private CommandParamRepository commandParamRepository;

    @Autowired
    private CommandInstanceParamRepository commandInstanceParamRepository;

    @Autowired
    private DataProcessFlowRepository flowRepository;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandInstanceFlowRelationRepository commandInstanceFlowRelationRepository;

    @Override
    @Transactional
    public void createCmdInstance(CommandInstanceVO vo) throws BusException {
        CommandInstanceEntity entity = new CommandInstanceEntity();
        entity.setCommandInstanceName(vo.getCommandInstanceName());
        String commandId = vo.getCmdId();
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        entity.setCommand(commandEntity);
        Map<String, String> cmdParamValueMap = vo.getCmdParamValue();
        List<DataProcessFlowEntity> flowEntities = flowRepository.findAllById(vo.getSubFlows());
        entity = commandInstanceRepository.save(entity);
        List<CommandInstanceFlowRelation> relations = new ArrayList<>();
        for (DataProcessFlowEntity flowEntity : flowEntities) {
            CommandInstanceFlowRelation relation = new CommandInstanceFlowRelation();
            relation.setFlowEntity(flowEntity);
            relation.setCommandInstanceEntity(entity);
            relations.add(relation);
        }
        commandInstanceFlowRelationRepository.saveAll(relations);
        List<CommandInstanceParamEntity> commandInstanceParamEntities = new ArrayList<>();
        Iterator<Map.Entry<String, String>> it = cmdParamValueMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            Optional<CommandParamEntity> optionalCommandParamEntity = commandParamRepository.findById(en.getKey());
            CommandParamEntity commandParamEntity1 = optionalCommandParamEntity.get();
            CommandInstanceParamEntity commandInstanceParamEntity = new CommandInstanceParamEntity();
            commandInstanceParamEntity.setParamValue(en.getValue());
            commandInstanceParamEntity.setCommandParamEntity(commandParamEntity1);
            commandInstanceParamEntity.setCommandInstanceEntity(entity);
            commandInstanceParamEntities.add(commandInstanceParamEntity);
        }
        commandInstanceParamRepository.saveAll(commandInstanceParamEntities);
    }

    @Override
    public void updateCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }

    @Override
    public void removeCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException {

    }
}
