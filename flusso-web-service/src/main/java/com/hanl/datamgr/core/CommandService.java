package com.hanl.datamgr.core;

import com.hanl.datamgr.entity.CommandEntity;
import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.CommandVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc: 命令服务
 */
@Service
public interface CommandService {


    /**
     * 创建命令
     *
     * @param entity
     */

    public void createCommand(CommandEntity entity) throws BusException;


    /**
     * 更新命令
     *
     * @param commandVO
     */
    public void updateCommand(CommandVO commandVO) throws BusException;


    /**
     * 移除命令
     *
     * @param commandVO
     */
    public void removeCommand(CommandVO commandVO) throws BusException;


    public List<CommandEntity> queryAllCommands() throws BusException;
}
