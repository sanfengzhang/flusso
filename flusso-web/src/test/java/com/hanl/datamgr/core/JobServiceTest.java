package com.hanl.datamgr.core;

import com.hanl.datamgr.Application;
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
 * @date :2019/11/6
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Test
    public void testGetJob() throws Exception {
        Map<String, Object> map = jobService.getJobConfig("8adb929b6dcf4089016dcf40b1b70002");
        List<Map<String, Object>> cmd=(List<Map<String, Object>>)map.get("flink.etl.morph_flow");
        MorphTest morphTest=new MorphTest();
        morphTest.testMorphlinMap(cmd,"数据处理流程测试1");
        System.out.println(map);
    }

}
