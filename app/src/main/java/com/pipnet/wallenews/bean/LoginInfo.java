package com.pipnet.wallenews.bean;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class LoginInfo{

    /**
     * followCount : 0
     * followedCount : 0
     * introduction : vip15656238290
     * isLogged : true
     * mobilePhoneNumber : 15656238290
     * nickName : vip15656238290
     * properties : [{"count":-1,"image":"http://www.mubbox.com/diting-web/upload/2018/10/11/09650c092e784967d243de7534612979_icon%20wd%20jf@3x.png","name":"个人积分","url":"http://www.mubbox.com/diting-h5/www/index.html#/integralDetail"},{"count":-1,"image":"http://www.mubbox.com/diting-web/upload/2018/10/11/9a1e3f5762c10cb64a558803750aa675_icon%20wd%20yf@3x.png","name":"邀请好友","url":"http://jd.com"},{"count":-1,"image":"http://www.mubbox.com/diting-web/upload/2018/10/11/e4a7394985c4ace736d6321589bc1246_icon%20wd%20dh@3x.png","name":"兑换记录","url":"http://baidu.com"},{"count":-1,"image":"http://www.mubbox.com/diting-web/upload/2018/10/11/3845bfa41d475fe8484874ffa30920bf_icon%20wd%20fk@3x.png","name":"问题反馈","url":"http://bing.com"}]
     * source : MOBILE
     * status : OK
     * uid : c9056ff3-135e-4145-9142-93297236ce11
     * userId : 1043
     * userName : vip15656238290
     */

    public int followCount;
    public int followedCount;
    public String introduction;
    public boolean isLogged;
    public String mobilePhoneNumber;
    public String nickName;
    public String source;
    public String status;
    public String error;
    public String uid;
    public int userId;
    public String userName;
    public List<PropertiesBean> properties;

    public static class PropertiesBean {
        /**
         * count : -1
         * image : http://www.mubbox.com/diting-web/upload/2018/10/11/09650c092e784967d243de7534612979_icon%20wd%20jf@3x.png
         * name : 个人积分
         * url : http://www.mubbox.com/diting-h5/www/index.html#/integralDetail
         */

        public int count;
        public String image;
        public String name;
        public String url;
    }
}
