package com.pipnet.wallenews.module.home;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;

public class FeedDetailActivity extends BaseActivity {

    long id = 0;

    @Override
    public int setContentView() {
        return R.layout.activity_feed_detail;
    }

    @Override
    public void initViewData() {
        id = getIntent().getLongExtra("FEED_ID", 0);
        NetRequest.detail(id, new BaseSubscriber<FeedDetailsInfo>() {
            @Override
            public void onNext(FeedDetailsInfo feedDetailsInfo) {

            }
        });

        NetRequest.replies(id, 1, new BaseSubscriber<RepliesResponse>() {
            @Override
            public void onNext(RepliesResponse repliesResponse) {

            }
        });

    }
}
