package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;
import com.pipnet.wallenews.util.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

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
    static final long serialVersionUID = -15515456L;
    public boolean hasRecommend;
    public String images;
    @Convert(columnType = String.class, converter = StringConverter.class)
    public List<String> imageArray;
    @Convert(columnType = String.class, converter = StringConverter.class)
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
    @Generated(hash = 1357875757)
    public ContentBean(boolean hasRecommend, String images, List<String> imageArray, List<String> sourceContentImageAry, int authorId, boolean authorIfFollowed, String authorImage, String authorName, String sourceAuthorName, String authorUUID, int commentCount, String content, String sourceContentTitle, long createTime, int forwardCount, String gif, long id, long sourceId, boolean ifForward, boolean ifLike, int likeCount, boolean newX, String subTitle, String title, long updateTime, String url, String video, String isAudit, String relType, String replyToIds,
            String replyTos) {
        this.hasRecommend = hasRecommend;
        this.images = images;
        this.imageArray = imageArray;
        this.sourceContentImageAry = sourceContentImageAry;
        this.authorId = authorId;
        this.authorIfFollowed = authorIfFollowed;
        this.authorImage = authorImage;
        this.authorName = authorName;
        this.sourceAuthorName = sourceAuthorName;
        this.authorUUID = authorUUID;
        this.commentCount = commentCount;
        this.content = content;
        this.sourceContentTitle = sourceContentTitle;
        this.createTime = createTime;
        this.forwardCount = forwardCount;
        this.gif = gif;
        this.id = id;
        this.sourceId = sourceId;
        this.ifForward = ifForward;
        this.ifLike = ifLike;
        this.likeCount = likeCount;
        this.newX = newX;
        this.subTitle = subTitle;
        this.title = title;
        this.updateTime = updateTime;
        this.url = url;
        this.video = video;
        this.isAudit = isAudit;
        this.relType = relType;
        this.replyToIds = replyToIds;
        this.replyTos = replyTos;
    }
    @Generated(hash = 1643641106)
    public ContentBean() {
    }
    public boolean getHasRecommend() {
        return this.hasRecommend;
    }
    public void setHasRecommend(boolean hasRecommend) {
        this.hasRecommend = hasRecommend;
    }
    public String getImages() {
        return this.images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public List<String> getImageArray() {
        return this.imageArray;
    }
    public void setImageArray(List<String> imageArray) {
        this.imageArray = imageArray;
    }
    public List<String> getSourceContentImageAry() {
        return this.sourceContentImageAry;
    }
    public void setSourceContentImageAry(List<String> sourceContentImageAry) {
        this.sourceContentImageAry = sourceContentImageAry;
    }
    public int getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public boolean getAuthorIfFollowed() {
        return this.authorIfFollowed;
    }
    public void setAuthorIfFollowed(boolean authorIfFollowed) {
        this.authorIfFollowed = authorIfFollowed;
    }
    public String getAuthorImage() {
        return this.authorImage;
    }
    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }
    public String getAuthorName() {
        return this.authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getSourceAuthorName() {
        return this.sourceAuthorName;
    }
    public void setSourceAuthorName(String sourceAuthorName) {
        this.sourceAuthorName = sourceAuthorName;
    }
    public String getAuthorUUID() {
        return this.authorUUID;
    }
    public void setAuthorUUID(String authorUUID) {
        this.authorUUID = authorUUID;
    }
    public int getCommentCount() {
        return this.commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSourceContentTitle() {
        return this.sourceContentTitle;
    }
    public void setSourceContentTitle(String sourceContentTitle) {
        this.sourceContentTitle = sourceContentTitle;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getForwardCount() {
        return this.forwardCount;
    }
    public void setForwardCount(int forwardCount) {
        this.forwardCount = forwardCount;
    }
    public String getGif() {
        return this.gif;
    }
    public void setGif(String gif) {
        this.gif = gif;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getSourceId() {
        return this.sourceId;
    }
    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }
    public boolean getIfForward() {
        return this.ifForward;
    }
    public void setIfForward(boolean ifForward) {
        this.ifForward = ifForward;
    }
    public boolean getIfLike() {
        return this.ifLike;
    }
    public void setIfLike(boolean ifLike) {
        this.ifLike = ifLike;
    }
    public int getLikeCount() {
        return this.likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public boolean getNewX() {
        return this.newX;
    }
    public void setNewX(boolean newX) {
        this.newX = newX;
    }
    public String getSubTitle() {
        return this.subTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getVideo() {
        return this.video;
    }
    public void setVideo(String video) {
        this.video = video;
    }
    public String getIsAudit() {
        return this.isAudit;
    }
    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }
    public String getRelType() {
        return this.relType;
    }
    public void setRelType(String relType) {
        this.relType = relType;
    }
    public String getReplyToIds() {
        return this.replyToIds;
    }
    public void setReplyToIds(String replyToIds) {
        this.replyToIds = replyToIds;
    }
    public String getReplyTos() {
        return this.replyTos;
    }
    public void setReplyTos(String replyTos) {
        this.replyTos = replyTos;
    }
}