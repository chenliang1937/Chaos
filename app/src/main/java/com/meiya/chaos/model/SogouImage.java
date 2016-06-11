package com.meiya.chaos.model;

import java.util.List;

/**
 * Created by chenliang3 on 2016/6/3.
 */
public class SogouImage {

    private String query;
    private List<theItems> items;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<theItems> getItems() {
        return items;
    }

    public void setItems(List<theItems> items) {
        this.items = items;
    }

    public class theItems{
        private String title;
        private String size;
        private String thumbUrl;
        private String smallThumbUrl;
        private String pic_url;
        private String thumb_width;
        private String thumb_height;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getSmallThumbUrl() {
            return smallThumbUrl;
        }

        public void setSmallThumbUrl(String smallThumbUrl) {
            this.smallThumbUrl = smallThumbUrl;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getThumb_width() {
            return thumb_width;
        }

        public void setThumb_width(String thumb_width) {
            this.thumb_width = thumb_width;
        }

        public String getThumb_height() {
            return thumb_height;
        }

        public void setThumb_height(String thumb_height) {
            this.thumb_height = thumb_height;
        }
    }

}
