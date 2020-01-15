package com.hanl.datalife.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Date;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/12/5
 * @desc:
 */
@Data
@NodeEntity
public class WideTableEntity {

    private Long id;

    private String tableName;//名称,kafka的topic的名称、es索引名称等

    private String tableType;//类型Kafka、ES、Mysql等

    private AppEntity createByApp;//谁创建的

    private AppEntity writeApp;//有哪些应用或者任务是可以写表的

    private AppEntity readApp;//有哪些应用或者任务是只读这个表

    private Date createDate;

    private Date updateDate;
}
