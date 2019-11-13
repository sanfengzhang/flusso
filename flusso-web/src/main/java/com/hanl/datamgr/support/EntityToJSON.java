package com.hanl.datamgr.support;

/**
 * @author: Hanl
 * @date :2019/10/19
 * @desc:
 */
public @interface EntityToJSON {


    ;

    Class<?>[] clazz() default {};

    String packageName() default "";
}
