package com.pipnet.wallenews.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LeeBoo on 17/8/21.
 * 消息通知的实体类
 */
public class ResponseObject<T> extends Response {

    /**
     * 返回结果
     */
    @SerializedName("data")
    public T result;
}
