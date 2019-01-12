package com.pipnet.wallenews.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LeeBoo on 17/8/18.
 * 接口返回实体类的基类
 */
public class Response {

    @SerializedName("errorCode")
    public String code;

    @SerializedName("errorMsg")
    public String msg;

    @SerializedName("error")
    public String error;

    public String status;

    public boolean success;

}
