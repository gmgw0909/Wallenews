package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthorBean {
    /**
     * canLogin : 1
     * createTime : 1540633976000
     * followCount : 0
     * followerCount : 1
     * id : 23
     * ifFollowed : false
     * image : http://www.mubbox.com/diting-web/upload/2018/10/27/e330e51b0346b33736dc29cbd030429f_xjb.jpeg
     * introduction : 新京报
     * latestContentId : 32165
     * latestContentImageArray : ["http://nimg.ws.126.net/?url=http%3A%2F%2Fcms-bucket.ws.126.net%2F2019%2F01%2F29%2F4d26c847ab7d428ba9c03661d687c769.jpg&thumbnail=690x2147483647&quality=75&type=jpg"]
     * latestContentSubTitle :
     * latestContentTitle : 野味贩子家中发现送检样本 森林公安:只是暂存
     * loginId : 1
     * name : 新京报
     * new : false
     * updateTime : 1540633976000
     * userUUID : 新京报
     */

    public int canLogin;
    public long createTime;
    public int followCount;
    public int followerCount;
    public int id;
    public boolean ifFollowed;
    public String image;
    public String introduction;
    public int latestContentId;
    public String latestContentSubTitle;
    public String latestContentTitle;
    public int loginId;
    public String name;
    @SerializedName("new")
    public boolean newX;
    public long updateTime;
    public String userUUID;
    public List<String> latestContentImageArray;
}
