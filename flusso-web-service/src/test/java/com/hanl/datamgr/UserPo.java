package com.hanl.datamgr;

import com.hanl.data.common.CommandDescription;
import com.hanl.data.common.CommandField;
import com.typesafe.config.Config;
import org.kitesdk.morphline.api.CommandBuilder;
import org.kitesdk.morphline.api.MorphlineContext;
import org.kitesdk.morphline.base.AbstractCommand;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */


public class UserPo {

    @CommandDescription(name = "用户算子", morphName = "User_Oper")
    private static final class User  extends AbstractCommand {
        @CommandField(fieldName = "userName", fieldType = "String")
        private String name;

        @CommandField(fieldName = "password", fieldType = "String")
        private String password;

        @CommandField(fieldName = "age", fieldType = "Int")
        private int age;

        public User(CommandBuilder builder, Config config, org.kitesdk.morphline.api.Command parent, org.kitesdk.morphline.api.Command child, MorphlineContext context) {
            super(builder, config, parent, child, context);
        }

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
}
