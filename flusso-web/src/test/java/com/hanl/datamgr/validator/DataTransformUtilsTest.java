package com.hanl.datamgr.validator;

import com.hanl.datamgr.Application;
import com.hanl.datamgr.entity.CommandInstanceParamEntity;
import com.hanl.datamgr.entity.FieldEntity;
import com.hanl.datamgr.repository.CommandInstanceParamRepository;
import com.hanl.datamgr.support.DataTransformUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/14
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataTransformUtilsTest {

    @Autowired
    private CommandInstanceParamRepository commandInstanceParamRepository;

    @Test
    public void testTransform() throws IllegalAccessException {
        List<CommandInstanceParamEntity> list = commandInstanceParamRepository.findAll();
        DataTransformUtils<String, CommandInstanceParamEntity> transformUtils = new DataTransformUtils<>();
        System.out.println(transformUtils.convert("fieldType", list.iterator()).toString());
    }

    @Test
    public void testTrans() throws Exception {
        FieldEntity commandParamEntity = new CommandInstanceParamEntity();
        commandParamEntity.setFieldType("12sda");
        Class clazz = (Class) commandParamEntity.getClass();
       // Field f = clazz.getDeclaredField("fieldType");
       // f.setAccessible(true);
        Field f1 = clazz.getSuperclass().getDeclaredField("fieldType");
        f1.setAccessible(true);

        System.out.println("-----");
        System.out.println(f1.get(commandParamEntity));

    }

}
