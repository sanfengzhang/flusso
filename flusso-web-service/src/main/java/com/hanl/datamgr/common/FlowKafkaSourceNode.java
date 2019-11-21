package com.hanl.datamgr.common;

import lombok.Data;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/11/6
 * @desc:
 */
@Data
public class FlowKafkaSourceNode extends FlowSourceNode {

    private String kafkaBroker;

    private String zookeeper;

    private String groupId;

    private List<String> topics;

    private String charset;

}
