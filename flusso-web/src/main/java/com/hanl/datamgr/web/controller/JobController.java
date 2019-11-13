package com.hanl.datamgr.web.controller;

import com.hanl.datamgr.core.JobService;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.web.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/10/24
 * @desc:
 */
@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/{id}")
    public CommonResponse getJobConfig(@PathVariable String id) throws BusException {
        Map<String, Object> jobParams = jobService.getJobConfig(id);
        return CommonResponse.buildWithSuccess(jobParams);
    }
}
