package com.pipnet.wallenews.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class FeedResponse {

    public int feedSize;
    public List<FeedsBean> feeds;
    public List<TopTopicBean> topTopic;

    public static class FeedsBean implements MultiItemEntity {
        /**
         * content : {"authorId":364,"authorImage":"","authorName":" 楚天都市报","authorUUID":" 楚天都市报","commentCount":0,"createTime":1548139077000,"forwardCount":0,"gif":"","hasRecommend":false,"id":29234,"ifForward":false,"ifLike":false,"imageArray":["http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg"],"images":"http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,","likeCount":0,"new":false,"subTitle":"","title":"女子23年前被弃湖北街头获美夫妻抚育 今回国寻亲","updateTime":1548139077000,"url":"https://c.m.163.com/news/a/E64LV5SO0001875P.html","video":"0"}
         * cursor : 150375
         * type : content
         */

        public ContentBean content;
        public int cursor;
        public String type;
        public boolean show;

        @Override
        public int getItemType() {
            if (!TextUtils.isEmpty(type) && type.equals("forward")) {
                return 1;
            } else {
                return 0;//默认recommendTopic
            }
        }
    }

    public static class TopTopicBean {
        /**
         * content : {"contentCount":8,"createTime":1542020218000,"followerCount":0,"id":71,"ifFollowed":false,"image":"http://www.mubbox.com/diting-web/empty","introduction":"游戏","isPrivate":0,"labelId":101,"latestContentId":29274,"latestContentImageArray":["https://pic.36krcnd.com/201901/22080116/ushs1nles6m1q6yu.png!heading","https://pic.36krcnd.com/201901/22080130/tnkjh4rwtdcevkoc.png!heading","https://pic.36krcnd.com/201901/22080302/ub2ima6gg5nb13ug.png!heading"],"latestContentSubTitle":"","latestContentTitle":"铲屎官请注意 支付宝上线\u201c宠物码\u201d小程序：可提高走丢宠物找回率","name":"游戏","new":false,"rank":0,"topicUUID":"游戏","updateTime":1542020218000}
         * cursor : 1044694
         * type : recommendTopic
         */

        public TopicsBean content;
        public int cursor;
        public String type;

        public TopTopicBean(TopicsBean content, String type) {
            this.content = content;
            this.type = type;
        }
    }
}
