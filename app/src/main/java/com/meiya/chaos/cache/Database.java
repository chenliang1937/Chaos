package com.meiya.chaos.cache;

import com.google.gson.reflect.TypeToken;
import com.meiya.chaos.common.App;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.model.GankItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public class Database {

    private static String DATA_FILE_NAME = "data.db";

    private static Database INSTANCE;

    File dataFile = new File(App.getContext().getFilesDir(), DATA_FILE_NAME);

    private Database(){

    }

    public static Database getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    public List<GankItem> readItems(){
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        try {
            Reader reader = new FileReader(dataFile);
            return AppService.getGson().fromJson(reader, new TypeToken<List<GankItem>>(){}.getType());
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public void writeItems(List<GankItem> items){
        String json = AppService.getGson().toJson(items);
        try {
            if (!dataFile.exists()){
                try {
                    dataFile.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void delete(){
        dataFile.delete();
    }

}
