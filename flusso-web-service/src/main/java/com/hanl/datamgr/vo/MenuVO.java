package com.hanl.datamgr.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/25
 * @desc:
 */
@Data
public class MenuVO<T> implements Serializable {

    private String id;

    private String name;

    private String type;

    private String ico;

    private String parentMenuId;

    private String alias;//别名

    private T data;

    private List<MenuVO> children = new ArrayList<>();

}
