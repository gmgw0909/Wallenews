package com.pipnet.wallenews.bean;

import java.util.List;

public class FollowResponse {
    public int feedSize;
    public List<Feeds> feeds;
    public List<String> topTopic;

    public static class Feeds {
        public List<AuthorBean> content;
        public long cursor;
        public String type;
    }
}
