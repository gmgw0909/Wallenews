package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LeeBoo on 2019/1/29.
 */

public class ContentBean implements Serializable {
    /**
     * authorId : 364
     * authorImage :
     * authorName :  楚天都市报
     * authorUUID :  楚天都市报
     * commentCount : 0
     * createTime : 1548139077000
     * forwardCount : 0
     * gif :
     * hasRecommend : false
     * id : 29234
     * ifForward : false
     * ifLike : false
     * imageArray : ["http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg"]
     * images : http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,
     * likeCount : 0
     * new : false
     * subTitle :
     * title : 女子23年前被弃湖北街头获美夫妻抚育 今回国寻亲
     * updateTime : 1548139077000
     * url : https://c.m.163.com/news/a/E64LV5SO0001875P.html
     * video : 0
     */

    public boolean hasRecommend;
    public String images;
    public List<String> imageArray;
    public List<String> sourceContentImageAry;
    public int authorId;
    public boolean authorIfFollowed;
    public String authorImage;
    public String authorName;
    public String sourceAuthorName;
    public String authorUUID;
    public int commentCount;
    public String content;
    public String sourceContentTitle;
    public long createTime;
    public int forwardCount;
    public String gif;
    public long id;
    public long sourceId;
    public boolean ifForward;
    public boolean ifLike;
    public int likeCount;
    @SerializedName("new")
    public boolean newX;
    public String subTitle;
    public String title;
    public long updateTime;
    public String url;
    public String video;
    public String isAudit;
    public String relType;
    public String replyToIds;
    public String replyTos;
}