package com.hanl.datamgr.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@Data
public class FlowSinkNode {

    private Map<String, Object> sinkConfig = new HashMap<>();

}
