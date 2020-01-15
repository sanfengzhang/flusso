package com.hanl.data.common;

import java.lang.annotation.*;

/**
 * @author: Hanl
 * @date :2020/1/15
 * @desc:
 */

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandField {

    public static final String FILED_NAME = "fieldName";

    public static final String FILED_TYPE = "fieldType";

    public static final String FILED_DESC = "fieldDesc";

    String fieldName();

    String fieldType();

    String fieldDesc() default "";


}
