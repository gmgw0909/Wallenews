package com.pipnet.wallenews.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FeedsBean implements MultiItemEntity {
    /**
     * content : {"authorId":364,"authorImage":"","authorName":" 楚天都市报","authorUUID":" 楚天都市报","commentCount":0,"createTime":1548139077000,"forwardCount":0,"gif":"","hasRecommend":false,"id":29234,"ifForward":false,"ifLike":false,"imageArray":["http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg","http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg"],"images":"http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F42gP%3DF3Eogitf8hifTPv6EjGuIxUnzpxjJz2lsPcLtHCv1548135887109compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FQ%3DsSYqMgJ69pkerhE4t1mvgKEXIV1Ei4F0MnkNGDIrMh%3D1548135887113.jpg&thumbnail=690x2147483647&quality=75&type=jpg,http://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2FSg4ma4ouxsabt7VUpzoW2eY5UWdDcYo6FQLXz6hFhyhnQ1548135887113compressflag.jpg&thumbnail=690x2147483647&quality=75&type=jpg,","likeCount":0,"new":false,"subTitle":"","title":"女子23年前被弃湖北街头获美夫妻抚育 今回国寻亲","updateTime":1548139077000,"url":"https://c.m.163.com/news/a/E64LV5SO0001875P.html","video":"0"}
     * cursor : 150375
     * type : content
     */
    @Convert(converter = ContentBeanConverter.class, columnType = String.class)
    public ContentBean content;
    @Unique
    public int cursor;
    public String type;
    public boolean show;

    @Generated(hash = 144462814)
    public FeedsBean(ContentBean content, int cursor, String type, boolean show) {
        this.content = content;
        this.cursor = cursor;
        this.type = type;
        this.show = show;
    }

    @Generated(hash = 1241035532)
    public FeedsBean() {
    }

    @Override
    public int getItemType() {
        if (!TextUtils.isEmpty(type) && type.equals("forward")) {
            return 1;
        } else {
            return 0;//默认recommendTopic
        }
    }

    public ContentBean getContent() {
        return this.content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getCursor() {
        return this.cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getShow() {
        return this.show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public static class ContentBeanConverter implements PropertyConverter<ContentBean, String> {
        @Override
        public ContentBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return new Gson().fromJson(databaseValue, ContentBean.class);
        }

        @Override
        public String convertToDatabaseValue(ContentBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return new Gson().toJson(entityProperty);
        }
    }
}
