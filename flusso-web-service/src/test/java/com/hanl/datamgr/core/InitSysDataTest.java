package com.hanl.datamgr.core;

import com.alibaba.fastjson.JSON;
import com.hanl.datamgr.Application;
import com.hanl.datamgr.common.MenuService;
import com.hanl.datamgr.entity.*;
import com.hanl.datamgr.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class InitSysDataTest {

    @Autowired
    private CommandService commandService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataProcessFlowRepository dataProcessFlowRepository;

    @Autowired
    private CommandInstanceRepository commandInstanceRepository;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private FlowLineRepository flowLineRepository;

    @Autowired
    private CanvasCommandInstanceRepository canvasCommandInstanceRepository;

    @Autowired
    private MenuService menuService;


    @Test
    public void testGetMenu() {
        System.out.println(JSON.toJSONString(menuService.getMainMenuData()));
    }

    @Test
    public void createCanvasCommandTest() {
        CanvasCommandInstanceEntity entity = new CanvasCommandInstanceEntity();
        entity.setCommandInstanceEntity(commandInstanceRepository.findById("8adb929b6dd3012b016dd301485b0000").get());
        entity.setTop("223px");
        entity.setLeft("851px");
        entity.setIco("el-icon-goods");
        entity.setShow(true);
        entity.setFirst(true);
        entity.setLast(false);
        canvasCommandInstanceRepository.save(entity);

        CanvasCommandInstanceEntity entity1 = new CanvasCommandInstanceEntity();
        entity1.setCommandInstanceEntity(commandInstanceRepository.findById("8adb929b6dec0ef0016dec0f08e30001").get());
        entity1.setTop("223px");
        entity1.setLeft("851px");
        entity1.setIco("el-icon-goods");
        entity1.setShow(true);
        entity1.setFirst(false);
        entity1.setLast(true);
        canvasCommandInstanceRepository.save(entity1);
    }


    @Test
    public void createFlowLineTest() {
        FlowLineEntity flowLine = new FlowLineEntity();
        // flowLine.setStart(commandInstanceRepository.findById("8adb929b6dd3012b016dd30148800009").get());
        // flowLine.setEnd(commandInstanceRepository.findById("8adb929b6dec0ef0016dec0f08e30001").get());
        //flowLine.setFlowEntity(dataProcessFlowRepository.findById("8adb929b6dcf4089016dcf40b16c0000").get());
        flowLineRepository.save(flowLine);
    }


    @Test
    public void createCmdInstanceTest() {
        CommandInstanceEntity commandInstanceEntity = new CommandInstanceEntity();
        commandInstanceEntity.setCommandInstanceName("SOC-分隔符解析-|");
        commandInstanceEntity.setCommand(commandRepository.findById("8adb929b6dcf3eb1016dcf3edb0e0000").get());

        CommandInstanceParamEntity inputField = new CommandInstanceParamEntity();
        inputField.setFieldName("inputField");
        inputField.setFieldType("java.lang.String");
        inputField.setFieldValue("message");
        inputField.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity outputFields = new CommandInstanceParamEntity();
        outputFields.setFieldName("outputFields");
        outputFields.setFieldType("java.util.List");
        outputFields.setFieldValue("[\"trans_date\",\"trans_code\",\"trans_channel_id\",\"trans_start_datetime\",\"trans_end_datetime\"," +
                "\"trans_cust_time\",\"trans_org_id\",\"trans_clerk\",\"trans_return_code\",\"trans_err_msg\",\"trans_tuexdo_name\"]");
        outputFields.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity separator = new CommandInstanceParamEntity();
        separator.setFieldName("separator");
        separator.setFieldType("java.lang.String");
        separator.setFieldValue("|");
        separator.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity isRegex = new CommandInstanceParamEntity();
        isRegex.setFieldName("isRegex");
        isRegex.setFieldType("java.lang.Boolean");
        isRegex.setFieldValue("false");
        isRegex.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity addEmptyStrings = new CommandInstanceParamEntity();
        addEmptyStrings.setFieldName("addEmptyStrings");
        addEmptyStrings.setFieldType("java.lang.Boolean");
        addEmptyStrings.setFieldValue("false");
        addEmptyStrings.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity trim = new CommandInstanceParamEntity();
        trim.setFieldName("trim");
        trim.setFieldType("java.lang.Boolean");
        trim.setFieldValue("false");
        trim.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity limit = new CommandInstanceParamEntity();
        limit.setFieldName("limit");
        limit.setFieldType("java.lang.Integer");
        limit.setFieldValue("11");
        limit.setCommandInstanceEntity(commandInstanceEntity);

        CommandInstanceParamEntity importCommands = new CommandInstanceParamEntity();
        importCommands.setFieldName("importCommands");
        importCommands.setFieldType("java.util.List");
        importCommands.setFieldValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");
        importCommands.setCommandInstanceEntity(commandInstanceEntity);

        Set<CommandInstanceParamEntity> commandInstanceParamEntityList = new HashSet<>();
        commandInstanceParamEntityList.add(inputField);
        commandInstanceParamEntityList.add(outputFields);
        commandInstanceParamEntityList.add(separator);
        commandInstanceParamEntityList.add(isRegex);
        commandInstanceParamEntityList.add(addEmptyStrings);
        commandInstanceParamEntityList.add(trim);
        commandInstanceParamEntityList.add(limit);
        commandInstanceParamEntityList.add(importCommands);
        commandInstanceEntity.setCmdInstanceParams(commandInstanceParamEntityList);


        CommandInstanceEntity commandInstanceEntity1 = new CommandInstanceEntity();
        commandInstanceEntity1.setCommandInstanceName("SOC-EL计算");
        commandInstanceEntity1.setCommand(commandRepository.findById("8adb929b6dcf3eb1016dcf3edb3f0001").get());

        CommandInstanceParamEntity cache_warming = new CommandInstanceParamEntity();
        cache_warming.setFieldName("cache_warming");
        cache_warming.setFieldType("java.util.Map");
        cache_warming.setFieldValue("{\"trans_return_code\":\"999\"}");
        cache_warming.setCommandInstanceEntity(commandInstanceEntity1);

        CommandInstanceParamEntity expresses = new CommandInstanceParamEntity();
        expresses.setFieldName("expresses");
        expresses.setFieldType("java.util.Map");
        expresses.setFieldValue("{\"trans_return_code<0 \"?\" 99999 \":\"trans_return_code\":\"java.lang.Integer,trans_return_code\"}");
        expresses.setCommandInstanceEntity(commandInstanceEntity1);

        CommandInstanceParamEntity importCommands1 = new CommandInstanceParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFieldValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");
        importCommands1.setCommandInstanceEntity(commandInstanceEntity1);


        Set<CommandInstanceParamEntity> commandInstanceParamEntityList1 = new HashSet<>();
        commandInstanceParamEntityList1.add(cache_warming);
        commandInstanceParamEntityList1.add(expresses);
        commandInstanceParamEntityList1.add(importCommands1);
        commandInstanceEntity1.setCmdInstanceParams(commandInstanceParamEntityList1);

        List<CommandInstanceEntity> data = new ArrayList<>();
        data.add(commandInstanceEntity);
        data.add(commandInstanceEntity1);
        commandInstanceRepository.saveAll(data);

    }



    @Test
    public void createSubFlow() {

        CommandEntity entity = new CommandEntity();
        entity.setCommandName("子流程节点");
        entity.setCommandClazz("com.stream.data.transform.command.CallSubPipeBuilder");
        entity.setCommandType("富化");
        entity.setCommandMorphName("callSubPipe");
        CommandInstanceEntity instanceEntity = new CommandInstanceEntity();
        instanceEntity.setCommand(entity);
        instanceEntity.setCommandInstanceName("子流程节点");
        //instanceEntity.setTop("223px");
        // instanceEntity.setLeft("851px");
        //instanceEntity.setIco("el-icon-goods");
        //instanceEntity.setShow(true);
        entity.getCommandInstanceEntityList().add(instanceEntity);

        CommandInstanceParamEntity paramEntity = new CommandInstanceParamEntity();
        paramEntity.setCommandInstanceEntity(instanceEntity);
        paramEntity.setFieldName("pipelineSelectorKey");
        paramEntity.setFieldType("java.lang.String");
        paramEntity.setFieldValue("keyValueSubPipeSelector");
        paramEntity.setCmdDisplayName("选择器名称");

        CommandInstanceParamEntity paramEntity1 = new CommandInstanceParamEntity();
        paramEntity1.setCommandInstanceEntity(instanceEntity);
        paramEntity1.setFieldName("continueParentPipe");
        paramEntity1.setFieldType("java.lang.Boolean");
        paramEntity1.setFieldValue("true");
        paramEntity1.setCmdDisplayName("是否继续父流程");


        CommandInstanceParamEntity importCommands1 = new CommandInstanceParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFieldValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");

        instanceEntity.getCmdInstanceParams().add(paramEntity);
        instanceEntity.getCmdInstanceParams().add(paramEntity1);
        instanceEntity.getCmdInstanceParams().add(importCommands1);
        commandRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    @Test
    public void createCmdFuhuaTest() {
        CommandEntity entity = new CommandEntity();
        entity.setCommandName("JAVA类-富化");
        entity.setCommandClazz("com.stream.data.transform.command.JavaMethodAddValueBuilder");
        entity.setCommandType("富化");
        entity.setCommandMorphName("javaMethodAddValue");
        CommandInstanceEntity instanceEntity = new CommandInstanceEntity();
        instanceEntity.setCommand(entity);
        instanceEntity.setCommandInstanceName("IP富化");
        //  instanceEntity.setTop("223px");
        // instanceEntity.setLeft("451px");
        // instanceEntity.setIco("el-icon-goods");
        //  instanceEntity.setShow(true);
        entity.getCommandInstanceEntityList().add(instanceEntity);

        CommandInstanceParamEntity paramEntity = new CommandInstanceParamEntity();
        paramEntity.setCommandInstanceEntity(instanceEntity);
        paramEntity.setFieldName("class_name");
        paramEntity.setFieldType("java.lang.String");
        paramEntity.setFieldValue("com.stream.data.transform.utils.IpaddressUtil");
        paramEntity.setCmdDisplayName("类名称");

        CommandInstanceParamEntity paramEntity1 = new CommandInstanceParamEntity();
        paramEntity1.setCommandInstanceEntity(instanceEntity);
        paramEntity1.setFieldName("method_name");
        paramEntity1.setFieldType("java.lang.String");
        paramEntity1.setFieldValue("getIplongValue");
        paramEntity1.setCmdDisplayName("方法名称");


        CommandInstanceParamEntity paramEntity3 = new CommandInstanceParamEntity();
        paramEntity3.setCommandInstanceEntity(instanceEntity);
        paramEntity3.setFieldName("original_key");
        paramEntity3.setFieldType("java.lang.String");
        paramEntity3.setFieldValue("ip");
        paramEntity3.setCmdDisplayName("富化字段名称");

        CommandInstanceParamEntity paramEntity4 = new CommandInstanceParamEntity();
        paramEntity4.setCommandInstanceEntity(instanceEntity);
        paramEntity4.setFieldName("derive_key");
        paramEntity4.setFieldType("java.lang.String");
        paramEntity4.setFieldValue("IPValue");
        paramEntity4.setCmdDisplayName("富化后字段名称");

        CommandInstanceParamEntity paramEntity5 = new CommandInstanceParamEntity();
        paramEntity5.setCommandInstanceEntity(instanceEntity);
        paramEntity5.setFieldName("argument_class");
        paramEntity5.setFieldType("java.lang.String");
        paramEntity5.setFieldValue("java.lang.String");
        paramEntity5.setCmdDisplayName("富化后字段类型");

        CommandInstanceParamEntity importCommands1 = new CommandInstanceParamEntity();
        importCommands1.setFieldName("importCommands");
        importCommands1.setFieldType("java.util.List");
        importCommands1.setFieldValue("[\"org.kitesdk.**\",\"com.stream.data.transform.command.*\"]");

        instanceEntity.getCmdInstanceParams().add(paramEntity);
        instanceEntity.getCmdInstanceParams().add(paramEntity1);
        instanceEntity.getCmdInstanceParams().add(paramEntity3);
        instanceEntity.getCmdInstanceParams().add(paramEntity4);
        instanceEntity.getCmdInstanceParams().add(paramEntity5);
        instanceEntity.getCmdInstanceParams().add(importCommands1);

        commandRepository.save(entity);
    }


    @Transactional(rollbackFor = Exception.class)
    public void createJobDataFlow() {
        DataProcessFlowEntity dataProcessFlowEntity = new DataProcessFlowEntity();
        dataProcessFlowEntity.setDataProcessFlowName("数据处理流程测试1");
        dataProcessFlowEntity.setCreateTime(new Date());
        dataProcessFlowEntity.setVersion(1);

        DataProcessFlowEntity dataProcessFlowEntity1 = new DataProcessFlowEntity();
        dataProcessFlowEntity1.setDataProcessFlowName("数据处理流程测试2");
        dataProcessFlowEntity1.setCreateTime(new Date());
        dataProcessFlowEntity1.setVersion(1);

        List<DataProcessFlowEntity> dataProcessFlowEntityList = new ArrayList<>();
        dataProcessFlowEntityList.add(dataProcessFlowEntity);
        dataProcessFlowEntityList.add(dataProcessFlowEntity1);
        dataProcessFlowRepository.saveAll(dataProcessFlowEntityList);

        JobEntity jobEntity = new JobEntity();
        jobEntity.setJobName("测试Job");
        jobEntity.setCreateTime(new Date());
        List<JobDataProcessFlowRelationEntity> jobEntities = jobEntity.getJobFlowRelSet();

        JobDataProcessFlowRelationEntity jobDataProcessFlowRelationEntity = new JobDataProcessFlowRelationEntity();
        jobDataProcessFlowRelationEntity.setJobEntity(jobEntity);
        jobDataProcessFlowRelationEntity.setDataProcessFlowEntity(dataProcessFlowEntity);
        jobEntities.add(jobDataProcessFlowRelationEntity);

        jobRepository.saveAndFlush(jobEntity);
    }

}
