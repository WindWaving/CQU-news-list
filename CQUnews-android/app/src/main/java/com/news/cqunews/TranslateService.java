/**
 * Translate service:
 * translate: make use of Baidu translation API
 * `HttpGet`/`MD5`/`TransApi` from package baidu.translate.demo are necessary for translation, they are from Baidu Translation demo, I've made some changes
 * call this by `startservice`
 */
package com.news.cqunews;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.baidu.translate.demo.TransApi;
import org.json.JSONArray;
import org.json.JSONObject;


/*
* translate between chinese and english
 */
public class TranslateService extends IntentService {
    //Baidu translation api
    private static final String APP_ID = "20200203000379866";
    private static final String SECURITY_KEY = "KWIRWXQhu3DLTfaF2HWG";
    private TransApi api;

    public TranslateService(){
        super("translating..");
        api=new TransApi(APP_ID, SECURITY_KEY);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TranslateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println(GetGlobals.CUR_LABEL);
        // the page is already in chinese
        if(GetGlobals.LANG.get(GetGlobals.CUR_LABEL)=="zh"){
            // EN_NEWS not updated
            if(GetGlobals.PAGE_INIT.get(GetGlobals.CUR_LABEL)==Boolean.FALSE){
                JSONArray arr=new JSONArray();//keep all the news, [{},{},{}....]
                for(int i=0;i<GetGlobals.NEWS_ARRAY.length();++i){
                    try {
                        JSONObject aNews = GetGlobals.NEWS_ARRAY.getJSONObject(i);
                        translate(aNews,arr);
                        Thread.currentThread().sleep(1200); // wait for the content translation finished
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                GetGlobals.EN_NEWS.put(GetGlobals.CUR_LABEL,arr);
                GetGlobals.NEWS_ARRAY=arr;
                GetGlobals.PAGE_INIT.put(GetGlobals.CUR_LABEL,Boolean.TRUE);
            }
            // EN_NEWS already updated
            else{
                GetGlobals.NEWS_ARRAY=GetGlobals.EN_NEWS.get(GetGlobals.CUR_LABEL);
            }
            GetGlobals.LANG.put(GetGlobals.CUR_LABEL,"en");
            System.out.println(GetGlobals.CUR_LABEL+" done");
        }
        // the page is in english
        else{
            GetGlobals.NEWS_ARRAY=GetGlobals.ZH_NEWS.get(GetGlobals.CUR_LABEL);
            System.out.println(GetGlobals.ZH_NEWS);
            GetGlobals.LANG.put(GetGlobals.CUR_LABEL,"zh");
        }

        Intent broadIntent=new Intent(NewsFragment.SERVICE_RECEIVER);
        getApplicationContext().sendBroadcast(broadIntent);
    }

    /**
     * the main part of translation
     * @param aNews: the jsonobject of a piece of news :{"title":"xx",....}
     * @param arr: the jsonarray to save all the news(aNews) :[{},{},{},...]
     */
    private void translate(final JSONObject aNews, JSONArray arr) {
        try {
            // translate into english
            String key="title";
            String value_en=api.getTransResult(aNews.getString(key),"zh","en");
            JSONObject jsonRes = new JSONObject(value_en);
            String res = jsonRes.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            aNews.put(key, res);

            Thread.currentThread().sleep(800);// wait till last translation finished

            String key1="digest";
            String value_en1=api.getTransResult(aNews.getString(key1),"zh","en");
            JSONObject jsonRes1 = new JSONObject(value_en1);
            String res1 = jsonRes1.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            System.out.println(res1 + aNews.getString("label"));
            aNews.put(key1, res1);

            Thread.currentThread().sleep(1000);

            String key2="content";
            String value_en2=api.getTransResult(aNews.getString(key2),"zh","en");
            JSONObject jsonRes2 = new JSONObject(value_en2);
            String res2 = jsonRes2.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            System.out.println(res2 + aNews.getString("label"));
            aNews.put(key2, res2);
            // push the english news into arr
            arr.put(aNews);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
