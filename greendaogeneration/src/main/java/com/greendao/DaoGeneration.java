package com.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGeneration {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(3, "com.meiya.greendao");
        addLatestNews(schema);
        addNewsDetail(schema);
        addVideoList(schema);
        addPicFlow(schema);
        new DaoGenerator().generateAll(schema, "../Chaos/app/src/main/java-gen");
    }

    public static void addLatestNews(Schema schema){
        Entity news = schema.addEntity("GreenNews");
        news.addIdProperty();
        news.addStringProperty("date");
        news.addStringProperty("newslist");
    }

    public static void addNewsDetail(Schema schema){
        Entity detail = schema.addEntity("GreenDetail");
        detail.addIdProperty();
        detail.addIntProperty("newsId");
        detail.addStringProperty("details");
    }

    public static void addVideoList(Schema schema){
        Entity video = schema.addEntity("GreenVideo");
        video.addIdProperty();
        video.addStringProperty("vid");
        video.addStringProperty("videolist");
    }

    public static void addPicFlow(Schema schema){
        Entity picflow = schema.addEntity("GreenPicflow");
        picflow.addIdProperty();
        picflow.addStringProperty("setid");
        picflow.addStringProperty("picflow");
    }

}
