package com.hanl.datamgr.core;

import com.hanl.datamgr.Application;
import com.hanl.data.transform.model.CommandPipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/16
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class CommandPipeLineServiceTest {

    @Autowired
    private CommandPipeLineService commandPipeLineService;

    @Test
    public void testPipelineTest() throws Exception {
        List<CommandPipeline> commandPipeline = commandPipeLineService.buildCommandPipeline("8adb929b6dcf4089016dcf40b16c0000");
        MorphTest morphTest = new MorphTest();
        morphTest.testMorphlin(commandPipeline, "数据处理流程测试1");
    }

    @Test
    public void testPipelineAddValueTest() throws Exception {
        List<CommandPipeline> commandPipeline = commandPipeLineService.buildCommandPipeline("8adb929b6e341b71016e343db7f90009");
        MorphTest morphTest = new MorphTest();
        morphTest.testMorphlin(commandPipeline, "|解析流程");
    }

    @Test
    public void testCommandListMapTest() throws Exception {
        List<Map<String, Object>> commandPipeline = commandPipeLineService.buildCommandMapConfig("8adb929b6dcf4089016dcf40b16c0000");
        System.out.println(commandPipeline);
        MorphTest morphTest = new MorphTest();
        morphTest.testMorphlinMap(commandPipeline, "数据处理流程测试1");
    }

    @Test
    public void testBranchPipelineTest() throws Exception {
        List<CommandPipeline> commandPipeline = commandPipeLineService.buildCommandPipeline("40288c816e2a5292016e31123c090008");
        MorphTest morphTest = new MorphTest();
        morphTest.testBranchMorphlin(commandPipeline, "分支流程测试流程5");
    }

}
