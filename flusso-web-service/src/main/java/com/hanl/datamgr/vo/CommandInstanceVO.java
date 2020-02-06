package com.hanl.datamgr.vo;

import com.hanl.datamgr.entity.CommandParamEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

/**
 * @author: Hanl
 * @date :2019/9/30
 * @desc:
 */
@Data
@ToString
public class CommandInstanceVO implements Serializable {

    private String cmdId;

    private String commandInstanceName;

    private Map<String, String> cmdParamValue = new HashMap<>();

    private List<String> subFlows = new ArrayList<>();

}
