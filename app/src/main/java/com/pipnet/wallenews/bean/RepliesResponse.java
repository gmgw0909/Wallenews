package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepliesResponse {

    public List<RepliesBean> replies;

    public static class RepliesBean {
        /**
         * authorId : 1042
         * authorImage : http://www.mubbox.com/diting-web/upload/2019/01/20/493f0a44b165622e175595dc465f537e_avatar.png
         * authorName : Av7obu
         * authorUUID : Av7obu
         * commentCount : 0
         * content : 呵呵
         * createTime : 1548154502000
         * forwardCount : 0
         * id : 556
         * isAudit : 0
         * likeCount : 0
         * new : false
         * relType : content
         * replyToIds :
         * replyTos :
         * sourceId : 29336
         * updateTime : 1548154502000
         */

        public int authorId;
        public String authorImage;
        public String authorName;
        public String authorUUID;
        public int commentCount;
        public String content;
        public long createTime;
        public int forwardCount;
        public int id;
        public String isAudit;
        public int likeCount;
        public boolean ifLike;
        @SerializedName("new")
        public boolean newX;
        public String relType;
        public String replyToIds;
        public String replyTos;
        public int sourceId;
        public long updateTime;
    }
}
