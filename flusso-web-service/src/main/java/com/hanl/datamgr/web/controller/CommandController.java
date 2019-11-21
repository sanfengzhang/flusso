package com.hanl.datamgr.web.controller;

import com.hanl.datamgr.core.CommandService;
import com.hanl.datamgr.entity.CommandEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/8
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/command")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @PostMapping
    public CommonResponse createCommand(@RequestBody CommandEntity commandEntity) throws BusException {
        commandService.createCommand(commandEntity);
        return CommonResponse.buildWithSuccess();
    }


    @GetMapping
    public CommonResponse queryAllCommand() throws BusException {
        List<CommandEntity> list= commandService.queryAllCommands();
        return CommonResponse.buildWithSuccess(list);
    }
}
