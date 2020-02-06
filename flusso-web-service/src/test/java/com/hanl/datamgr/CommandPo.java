package com.hanl.datamgr;

import org.kitesdk.morphline.api.CommandDescription;
import org.kitesdk.morphline.api.CommandParam;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
@CommandDescription(name = "IP富化算子", morphName = "IP_Transform",cmdType = "")
public class CommandPo {

    @CommandParam(paramName = "userName", paramType = "String")
    private String name;

    @CommandParam(paramName = "password", paramType = "String")
    private String password;

    @CommandParam(paramName = "age", paramType = "Int")
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
