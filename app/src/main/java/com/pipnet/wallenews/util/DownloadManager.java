package com.pipnet.wallenews.util;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by circle on 2017/9/27.
 */

public class DownloadManager {
    private String mSavePath;
    private String downloadUrl;
    private DownloadListener mListener;
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    public static final int DOWNLOAD_SUCC = 2;
    public static final int DOWNLOAD_FAILED = 3;

    /* 记录进度条数量 */
    private int progress;

    public DownloadManager(String downloadUrl, DownloadListener listener) {
        this.downloadUrl = downloadUrl.replace("'", "");
        this.mListener = listener;
        new downloadApkThread().start();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    //开始下载
                    break;
                case DOWNLOAD_SUCC:
                    File apkfile = new File(mSavePath, "xlb_" + downloadUrl.hashCode() + "");
                    if (!apkfile.exists()) {
                        mListener.downloadResult(DOWNLOAD_FAILED, null);
                        return;
                    }
                    File realFile = new File(mSavePath, "xlb_" + downloadUrl.hashCode() + ".png");
                    if (apkfile.renameTo(realFile))
                        mListener.downloadResult(DOWNLOAD_SUCC, realFile);
                    else
                        mListener.downloadResult(DOWNLOAD_SUCC, apkfile);

                    break;
                case DOWNLOAD_FAILED:
                    mListener.downloadResult(DOWNLOAD_FAILED, null);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                mSavePath = FileUtils.getDirpath() + "image";
                URL url = new URL(downloadUrl);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                long length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                // 获取文件大小

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdirs();
                }
                File apkFile = new File(mSavePath, "xlb_" + downloadUrl.hashCode() + "");

                if (apkFile.exists()) {
                    apkFile.deleteOnExit();
                } else {
                    apkFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(apkFile);
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                int count = 0;
                int numread;

                while ((numread = bis.read(buf)) != -1) {
                    fos.write(buf, 0, numread);

                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);

                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                }

                fos.close();
                is.close();
                if (progress == 100) {
                    // 下载完成
                    mHandler.sendEmptyMessage(DOWNLOAD_SUCC);
                } else {
                    //下载失败
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                }

            } catch (Exception e) {
                //下载失败
                mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                e.printStackTrace();
            }
        }
    }


    public interface DownloadListener {
        public void downloadResult(int state, File file);
    }

}
