package com.news.cqunews;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateService extends IntentService {

//    public static JSONArray NEWS_ARRAY;
    public UpdateService(){
        super("create a servcie");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        String label=intent.getStringExtra("label");//get value from intent
        String label=GetGlobals.CUR_LABEL;
//        System.out.println(label);
        try {
            GetGlobals.NEWS_ARRAY=NewsHttpUtil.reqNews(label);//send request to get new data
            Intent broadIntent=new Intent(NewsFragment.SERVICE_RECEIVER);
            getApplicationContext().sendBroadcast(broadIntent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

