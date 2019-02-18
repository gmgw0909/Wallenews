package com.pipnet.wallenews.bean;

import java.util.List;

public class FeedResponse {

    public int feedSize;
    public List<FeedsBean> feeds;
    public List<TopTopicBean> topTopic;

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
