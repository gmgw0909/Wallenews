package com.pipnet.wallenews.http.subscriber;

import com.pipnet.wallenews.App;
import com.pipnet.wallenews.util.NetUtil;
import com.pipnet.wallenews.util.XLog;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/**
 * Created by LeeBoo on 2018/12/7.
 */

public abstract class BaseSubscriber<T> extends ResourceSubscriber<T> {

    private String mErrorMsg;

    protected BaseSubscriber() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            switch (((HttpException) e).code()) {
                case 403:
                    mErrorMsg = "没有权限访问此链接！";
                    break;
                case 504:
                    if (!NetUtil.isConnected(App.getInstance())) {
                        mErrorMsg = "没有联网哦！";
                    } else {
                        mErrorMsg = "网络连接超时！";
                    }
                    break;
                default:
                    mErrorMsg = ((HttpException) e).message();
                    break;
            }
        } else if (e instanceof UnknownHostException) {
            mErrorMsg = "不知名主机！";
        } else if (e instanceof SocketTimeoutException) {
            mErrorMsg = "网络连接超时！";
        } else {
            mErrorMsg = e.toString();
        }
        XLog.e(mErrorMsg);
    }

    @Override
    public void onComplete() {

    }
}
