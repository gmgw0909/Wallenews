package com.pipnet.wallenews.bean;

import java.util.List;

public class FollowResponse {
    public int feedSize;
    public List<Feeds> feeds;
    public List<String> topTopic;

    public static class Feeds {
        public List<Content> content;
        public long cursor;
        public String type;
    }

    public static class Content {
        public int canLogin;
        public long createTime;
        public int followCount;
        public int followerCount;
        public int id;
        public boolean ifFollowed;
        public String image;
        public String introduction;
        public int latestContentId;
        public List<String> latestContentImageArray;
        public String latestContentSubTitle;
        public String latestContentTitle;
        public int loginId;
        public String name;
        public long updateTime;
        public String userUUID;
    }
}
