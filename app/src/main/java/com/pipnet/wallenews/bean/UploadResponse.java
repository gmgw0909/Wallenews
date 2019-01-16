package com.pipnet.wallenews.bean;

import java.util.List;

public class UploadResponse {

    public List<Files> files;

    public static class Files {
        public String delete_type;
        public String delete_url;
        public String error;
        public String name;
        public int size;
        public String thumbnail_url;
        public String url;
    }
}