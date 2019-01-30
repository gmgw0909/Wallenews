package com.pipnet.wallenews.bean;

import java.util.List;

public class SearchRecommend {

    public List<TagBean> hotTopics;
    public List<TagBean> suggTopics;
    public List<TagBean> feeds;

    public static class TagBean {
        /**
         * name : 孙杨
         */

        public String name;

        public TagBean(String name) {
            this.name = name;
        }
    }
}
