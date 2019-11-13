package com.hanl.datamgr.core;

import com.hanl.datamgr.Application;
import com.hanl.datamgr.entity.CommandInstanceParamEntity;
import com.hanl.datamgr.entity.CommandParamEntity;
import com.hanl.datamgr.repository.CommandInstanceParamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class CreateFiledParamTest {

    @Autowired
    private CommandInstanceParamRepository repository;

    @Test
    public void testExample() {

        CommandParamEntity commandParamEntity=new CommandParamEntity();
        commandParamEntity.setFieldName("userName");
        commandParamEntity.setFieldType("java.lang.String");
        commandParamEntity.setCmdDisplayName("用户名称");

        CommandInstanceParamEntity instanceParamEntity = new CommandInstanceParamEntity();
        instanceParamEntity.setCommandParamEntity(commandParamEntity);
        instanceParamEntity.setFieldValue("zhangsan");

        repository.save(instanceParamEntity);

    }

}
