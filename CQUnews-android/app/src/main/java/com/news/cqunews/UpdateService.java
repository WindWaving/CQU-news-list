/**
 * update service:
 * to update the news list from server
 * call this by `startservice`
 */
package com.news.cqunews;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class UpdateService extends IntentService {

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
        String label=GetGlobals.CUR_LABEL;
        System.out.println("update service");
        try {
            GetGlobals.NEWS_ARRAY=NewsHttpUtil.reqNews(label);//send request to get new data
            GetGlobals.PAGE_INIT.put(GetGlobals.CUR_LABEL,Boolean.FALSE);// the en_news should update
            Intent broadIntent=new Intent(NewsFragment.SERVICE_RECEIVER);
            getApplicationContext().sendBroadcast(broadIntent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

