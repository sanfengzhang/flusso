package com.hanl.datamgr.common;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@Data
public class FlowSocketSourceNode extends FlowSourceNode {

    private String host;

    private String port;

    private String dataType;

}
