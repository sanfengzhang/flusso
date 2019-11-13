package com.hanl.datamgr.core;

import com.hanl.datamgr.exception.BusException;
import com.hanl.datamgr.vo.CommandInstanceVO;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
public interface CommandInstanceService {

    public void createCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

    public void updateCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

    public void removeCmdInstance(CommandInstanceVO commandInstanceVO) throws BusException;

}
