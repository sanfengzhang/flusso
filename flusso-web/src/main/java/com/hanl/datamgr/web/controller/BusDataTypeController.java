package com.hanl.datamgr.web.controller;

import com.hanl.datamgr.core.DataTypeService;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.BusDataTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: Hanl
 * @date :2019/10/8
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/data_type")
public class BusDataTypeController {

    @Autowired
    private DataTypeService dataTypeService;

    @RequestMapping(method = RequestMethod.POST)
    public String createDataTypeRequest(@Valid BusDataTypeVO busDataTypeVO) {
        System.out.println(busDataTypeVO);
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String queryDataTypeRequest(@RequestParam String name)throws BusException {
        if(name.equals("zhangsan")){
            throw  new BusException("参数错误");
        }
        return "success";
    }

    @RequestMapping(value = "import",method = RequestMethod.POST)
    public String importDataTypeRequest()throws BusException {
        dataTypeService.createDataType(new BusDataTypeVO());
        return "success";
    }

}
