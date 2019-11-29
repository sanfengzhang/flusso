package com.hanl.datamgr.entity;

/**
 * @author: Hanl
 * @date :2019/11/29
 * @desc: 广泛的表的定义，一个数据源如：kafka某个topic，Mysql某张表、ES某张表等等都可以定义为
 * 一张业务意义上的"表"
 */
public class WideTableEntity {

    private String id;

    private String tableName;

    private String originType;//源类型如：kafka,es,mysql等等

    private String visitInfo;//访问该表方式，就是可以获取具体表的信息

}
