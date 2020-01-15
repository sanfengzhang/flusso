package com.hanl.datalife.entity;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * @author: Hanl
 * @date :2019/12/10
 * @desc:
 */
@Data
@Builder
@RelationshipEntity(type = "supply")
public class AppAndTableRelation {

    @Id
    @GeneratedValue
    private Long id;

    //关系的一端节点是 表示应用的节点
    @StartNode
    private AppEntity appNode;


    //关系的一端节点是 表示表的节点
    @EndNode
    private WideTableEntity wideTableNoe;

}
