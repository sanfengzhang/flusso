package com.hanl.datamgr.core;

import com.hanl.datamgr.Application;
import com.hanl.datamgr.exception.BusException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@Rollback(false)
public class CommandRegisterServiceTest {

    @Autowired
    private CommandRegisterService commandRegisterService;

    @Test
    public void testPrintAnntation() throws BusException {

        commandRegisterService.registerCommands("classes");
    }
}
