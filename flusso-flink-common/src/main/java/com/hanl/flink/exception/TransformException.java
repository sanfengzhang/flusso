package com.hanl.flink.exception;

/**
 * @author: Hanl
 * @date :2019/8/30
 * @desc:
 */
public class TransformException extends RuntimeException {

    public TransformException(String message) {

        super(message);
    }

    public TransformException(String message, Throwable cause) {

        super(message, cause);
    }
}
