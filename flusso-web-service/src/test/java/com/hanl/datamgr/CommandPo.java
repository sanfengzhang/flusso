package com.hanl.datamgr;

import com.hanl.data.common.CommandDescription;
import com.hanl.data.common.CommandField;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
@CommandDescription(name = "IP富化算子", morphName = "IP_Transform")
public class CommandPo {

    @CommandField(fieldName = "userName", fieldType = "String")
    private String name;

    @CommandField(fieldName = "password", fieldType = "String")
    private String password;

    @CommandField(fieldName = "age", fieldType = "Int")
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
