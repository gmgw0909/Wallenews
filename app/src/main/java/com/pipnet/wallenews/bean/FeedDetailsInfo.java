package com.pipnet.wallenews.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedDetailsInfo {

    /**
     * content : {"authorId":109,"authorIfFollowed":false,"authorImage":"http://cms-bucket.nosdn.127.net/88b7e1fada064b0da4491a068b674c2a20180226194441.jpeg","authorName":"证券时报网","authorUUID":"证券时报网","commentCount":0,"content":"<!DOCTYPE HTML>\r\n<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n<meta http-equiv='X-UA-Compatible' content='IE=edge'>\r\n<meta name='viewport' content='width=device-width, initial-scale=1,user-scalable=no'>\r\n<title><\/title>\r\n<style>\r\n\t.avater{\r\n\t\twidth:8%; \r\n\t\theight:8%; \r\n\t\tmargin-bottom:-6px;\r\n\t\tborder-radius:50%; \r\n\t\toverflow:hidden;\r\n\t}\r\n\t.g-title{\r\n\t\tfont-size:21px;\r\n\t\t}\r\n\tspan{\r\n\t\tfont-size:16px;\r\n\t}\r\n\tp span{\r\n\t\tfont-size:18px;\r\n\t}\r\n\tspan.reply{\r\n\t\tcolor:red;\r\n\t}\r\n\tb{\r\n\t\tfont-size:16px;\r\n\t}\r\n\tp b{\r\n\t\tfont-size:18px;\r\n\t\tline-height: 30px;\r\n\t\t-webkit-margin-before: 1em;\r\n    -webkit-margin-after: 1em;\r\n    -webkit-margin-start: 0px;\r\n    -webkit-margin-end: 0px;\r\n\t\t}\r\n\tmain,p{\r\n\t\tword-break:break-word;letter-spacing:0.8px;line-height:1.6;text-align: justify;\r\n\t\tfont-size:18px;\r\n\t}\r\n\timg{\r\n\t\twidth:100%;\r\n\t\tmargin-top:20px;\r\n\t}\r\n\tfigcaption{\r\n\t\tfont-size:10px;\r\n\t\tcolor:gray;\r\n\t\ttext-align:center;\r\n\t}\r\na{color:blue;text-decoration: none;display:block;}\r\ntable{width:100%;}\r\nheader{\r\n\tfont-size:18px;\r\n\tfont-weight:bold;\r\n\tmargin-bottom:10px;\r\n\t}\r\n<\/style>\r\n<\/head>\r\n<body><main><p>1月22日，华为就美国将向加拿大正式提出引渡孟晚舟一事回应记者表示：\u201c我们已经注意到相关报道，并将密切关注事件进展。华为遵守业务所在国的所有适用法律法规，包括联合国、美国和欧盟适用的出口管制和制裁法律法规。华为希望美国和加拿大政府能早日还孟女士以自由，并相信加拿大和美国的法律体系后续会给出公正的结论。\u201d<\/p><p>原标题：华为回应美国引渡孟晚舟一事：希望美国和加拿大政府能早日还孟女士以自由<\/p><p><!--viewpoint--><\/p><div class=\"m-newsapplite-redpacket-card\"><\/div><\/body><\/html>","createTime":1548146189000,"forwardCount":0,"gif":"","id":29287,"ifForward":false,"ifLike":false,"likeCount":0,"new":false,"subTitle":"","title":"华为回应美将引渡孟晚舟:望美加政府早日还其自由","updateTime":1548146189000,"url":"https://c.m.163.com/news/a/E64UE49H0001875N.html","video":"0"}
     * replies : []
     * topics : [{"contentCount":0,"createTime":1544675598000,"followerCount":0,"id":80,"ifFollowed":false,"image":"http://www.mubbox.com/diting-web/upload/2018/12/13/6268fb3a56f30d6a331bc55191c6ba69_mwz.jpeg","introduction":"孟晚舟","isPrivate":0,"labelId":7742,"latestContentId":29317,"latestContentImageArray":["http://nimg.ws.126.net/?url=http%3A%2F%2Fcms-bucket.ws.126.net%2F2019%2F01%2F22%2F8a22798351834146b2e0ad0543bdfe73.jpg&thumbnail=690x2147483647&quality=75&type=jpg"],"latestContentSubTitle":"","latestContentTitle":"媒体:拒绝向美引渡孟晚舟 是加拿大应作的抉择","name":"孟晚舟","new":false,"rank":0,"topicUUID":"孟晚舟","updateTime":1544675598000},{"contentCount":407,"createTime":1544801880000,"followerCount":0,"id":83,"ifFollowed":false,"image":"http://www.mubbox.com/diting-web/upload/2018/12/14/ed12aa4ba20b5658a25f91db8563bca9_hw.jpeg","introduction":"华为","isPrivate":0,"labelId":1784,"latestContentId":29296,"latestContentSubTitle":"","latestContentTitle":"华为回应美正计划引渡孟晚舟：望美加政府早日还孟晚舟以自由","name":"华为","new":false,"rank":0,"topicUUID":"华为","updateTime":1544801880000}]
     */

    public ContentBean content;
    public List<?> replies;
    public List<TopicsBean> topics;

    public static class ContentBean {
        /**
         * authorId : 109
         * authorIfFollowed : false
         * authorImage : http://cms-bucket.nosdn.127.net/88b7e1fada064b0da4491a068b674c2a20180226194441.jpeg
         * authorName : 证券时报网
         * authorUUID : 证券时报网
         * commentCount : 0
         * content : <!DOCTYPE HTML>
         <html>
         <head>
         <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
         <meta http-equiv='X-UA-Compatible' content='IE=edge'>
         <meta name='viewport' content='width=device-width, initial-scale=1,user-scalable=no'>
         <title></title>
         <style>
         .avater{
         width:8%;
         height:8%;
         margin-bottom:-6px;
         border-radius:50%;
         overflow:hidden;
         }
         .g-title{
         font-size:21px;
         }
         span{
         font-size:16px;
         }
         p span{
         font-size:18px;
         }
         span.reply{
         color:red;
         }
         b{
         font-size:16px;
         }
         p b{
         font-size:18px;
         line-height: 30px;
         -webkit-margin-before: 1em;
         -webkit-margin-after: 1em;
         -webkit-margin-start: 0px;
         -webkit-margin-end: 0px;
         }
         main,p{
         word-break:break-word;letter-spacing:0.8px;line-height:1.6;text-align: justify;
         font-size:18px;
         }
         img{
         width:100%;
         margin-top:20px;
         }
         figcaption{
         font-size:10px;
         color:gray;
         text-align:center;
         }
         a{color:blue;text-decoration: none;display:block;}
         table{width:100%;}
         header{
         font-size:18px;
         font-weight:bold;
         margin-bottom:10px;
         }
         </style>
         </head>
         <body><main><p>1月22日，华为就美国将向加拿大正式提出引渡孟晚舟一事回应记者表示：“我们已经注意到相关报道，并将密切关注事件进展。华为遵守业务所在国的所有适用法律法规，包括联合国、美国和欧盟适用的出口管制和制裁法律法规。华为希望美国和加拿大政府能早日还孟女士以自由，并相信加拿大和美国的法律体系后续会给出公正的结论。”</p><p>原标题：华为回应美国引渡孟晚舟一事：希望美国和加拿大政府能早日还孟女士以自由</p><p><!--viewpoint--></p><div class="m-newsapplite-redpacket-card"></div></body></html>
         * createTime : 1548146189000
         * forwardCount : 0
         * gif :
         * id : 29287
         * ifForward : false
         * ifLike : false
         * likeCount : 0
         * new : false
         * subTitle :
         * title : 华为回应美将引渡孟晚舟:望美加政府早日还其自由
         * updateTime : 1548146189000
         * url : https://c.m.163.com/news/a/E64UE49H0001875N.html
         * video : 0
         */

        public int authorId;
        public boolean authorIfFollowed;
        public String authorImage;
        public String authorName;
        public String authorUUID;
        public int commentCount;
        public String content;
        public long createTime;
        public int forwardCount;
        public String gif;
        public int id;
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
    }
}
