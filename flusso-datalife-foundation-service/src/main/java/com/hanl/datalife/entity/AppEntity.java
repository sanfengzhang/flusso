package com.hanl.datalife.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * @author: Hanl
 * @date :2019/12/5
 * @desc:
 */
@Data
@NodeEntity
public class AppEntity {

    private Long id;

    private String appName;

    private WideTableEntity writeTable;//应用产生哪些数据，写哪些表

    private WideTableEntity  readTable;//应用会读取哪些数据参与计算

    private Dept dept;

    private String appDesc;

}
