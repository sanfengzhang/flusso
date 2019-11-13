package com.hanl.datamgr.support;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Hanl
 * @date :2019/9/27
 * @desc:
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlreadyInDBConstraintValidator.class)
public @interface AlreadyInDB {

    String table() default "";

    String where() default "";

    String sql();

    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};

    String message() default "数据库记录已经存在";
}
