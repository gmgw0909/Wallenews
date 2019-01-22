package com.pipnet.wallenews.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.pipnet.wallenews.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static String DIR_NAME = "wali/";
    /**
     * 将图片转换成Base64编码的字符串
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private static String getRootPath() {
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/";
        } else {
            return App.getInstance().getCacheDir().getAbsolutePath() + "/android/data/" + App.getInstance().getPackageName() + "/";
        }
    }

    public static String getDirpath() {
        String filePath = getRootPath() + DIR_NAME;
        File f = new File(filePath);
        if (!f.exists())
            f.mkdir();
        return filePath;
    }

}
