package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.JobVO;

import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
public interface JobService {

    /**
     * @param jobVO
     * @throws BusException
     */
    public void crateJob(JobVO jobVO) throws BusException;

    /**
     * @param jobVO
     * @throws BusException
     */
    public void updateJob(JobVO jobVO) throws BusException;

    /**
     * @param jobVO
     * @throws BusException
     */
    public void removeJob(JobVO jobVO) throws BusException;

    /**
     * @throws BusException
     */
    public void queryJobs() throws BusException;

    /**
     *
     * @param jobId
     * @throws BusException
     */
    public Map<String, Object> getJobConfig(String jobId) throws BusException;

}
