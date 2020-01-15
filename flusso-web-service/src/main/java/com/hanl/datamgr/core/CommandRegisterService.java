package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc:
 */
public interface CommandRegisterService {

    /**
     * 通过jar文件上传的方式直接创建命令
     * @param commandJarFilePath
     * @throws BusException
     */
    public void registerCommands(String... commandJarFilePath) throws BusException;


}
