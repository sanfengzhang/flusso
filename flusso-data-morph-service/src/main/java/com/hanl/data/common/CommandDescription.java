package com.hanl.data.common;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author: Hanl
 * @date :2020/1/16
 * @desc:
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface CommandDescription {

    public static final String NAME = "name";

    public static final String MORPH_NAME = "morphName";

    String name();

    String morphName();
}
