package com.hanl.datamgr.web.controller;

import com.hanl.datamgr.common.MenuService;
import com.hanl.datamgr.core.CommandService;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.MenuVO;
import com.hanl.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/11
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/main")
public class MainPageController {

    @Autowired
    private CommandService commandService;

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public CommonResponse getMainMenu() throws BusException {
        List<MenuVO<String>> leftMenuVOList = menuService.getMainMenuData();
        return CommonResponse.buildWithSuccess(leftMenuVOList);
    }
}
