package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicsBean {
    public int contentCount;
    public long createTime;
    public int followerCount;
    public int id;
    public boolean ifFollowed;
    public String image;
    public String introduction;
    public int isPrivate;
    public int labelId;
    public int latestContentId;
    public String latestContentSubTitle;
    public String latestContentTitle;
    public String name;
    @SerializedName("new")
    public boolean newX;
    public int rank;
    public String topicUUID;
    public long updateTime;
    public List<String> latestContentImageArray;
}
