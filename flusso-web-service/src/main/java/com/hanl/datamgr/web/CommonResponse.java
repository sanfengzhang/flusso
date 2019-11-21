package com.hanl.datamgr.web;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Data
@ToString
public class CommonResponse implements Serializable {

    private Object data;

    private Object errResult;

    public static CommonResponse buildWithSuccess(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setData(data);
        return commonResponse;
    }

    public static CommonResponse buildWithSuccess() {
        CommonResponse commonResponse = new CommonResponse();
        return commonResponse;
    }

    public static CommonResponse buildWithException(Object errResult) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setErrResult(errResult);
        return commonResponse;
    }

}
