package com.hanl.datamgr.web.controller;

import com.hanl.datamgr.core.DataProcessFlowService;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.FlowVO;
import com.hanl.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/17
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/flow")
public class DataFlowController {

    @Autowired
    private DataProcessFlowService flowService;

    @GetMapping("/{id}")
    public CommonResponse findDataFlowById(@PathVariable String id) throws BusException {
        List<FlowVO> result = flowService.queryDataProcessFlows(id);
        if (result.size() > 0) {
            return CommonResponse.buildWithSuccess(result.get(0));
        } else {
            return CommonResponse.buildWithSuccess();
        }
    }

    @GetMapping
    public CommonResponse findAllDataFlow() throws BusException {

        return CommonResponse.buildWithSuccess(flowService.queryDataProcessFlows(null));
    }

    @RequestMapping(method = RequestMethod.POST)
    public CommonResponse saveDataFlow(@RequestBody @Valid FlowVO flowVORequest) throws BusException {
        flowService.saveDataProcessFlow(flowVORequest);
        return CommonResponse.buildWithSuccess();
    }

    @RequestMapping(value = "/relation", method = RequestMethod.POST)
    public CommonResponse saveDataFlowRelation(@RequestBody FlowVO flowVORequest) throws BusException {
        flowService.saveFlowLineRelation(flowVORequest);
        return CommonResponse.buildWithSuccess();
    }


    @PostMapping(value = "/{id}/debug")
    public CommonResponse debugFlow(@PathVariable String id, @RequestBody String data) throws BusException {
        flowService.debugFlow(id, data);
        return CommonResponse.buildWithSuccess();
    }

}
