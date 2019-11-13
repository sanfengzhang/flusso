package com.hanl.datamgr.exception;

/**
 * @author: Hanl
 * @date :2019/9/29
 * @desc:
 */
public class BusException extends Exception {

    public BusException(String message) {

        super(message, null);
    }

    public BusException(String message, Throwable cause) {

        super(message, cause);
    }
}