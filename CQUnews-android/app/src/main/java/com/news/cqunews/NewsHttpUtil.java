package com.news.cqunews;

import android.content.Context;
import android.icu.util.LocaleData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* server returns the json:
* [{"model":"xxx", "pk":xxx,"fields":{"title":"xxx", "author":"xxx", "date":"xxx", "digest":"xxx", "content":"xxx","label":"xx"}},
        {...},{...},...
        ]
* */
public class NewsHttpUtil {

    /*
    * get news labeled 'comprehensive news'
    * @params:
    *       context: the context of current activity
    *       serverUrl: the request address of server
    * @return:
    *       the news list as JSONArray:[{"title":"xx",...},{},...]
    * */
    public static JSONArray reqNews(final String label) throws InterruptedException {
        final JSONArray[] newsArr = {new JSONArray()};

        Thread subThread =new Thread(new Runnable() {
            @Override
            public void run() {
                String baseUrl="http://192.168.101.2:8780/";//the localhost in genymotion
                String serverUrl="";
                // request different address according to the label
                switch (label) {
                    case "综合新闻":
                        serverUrl = baseUrl + "comprehensive/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "教学科研":
                        serverUrl = baseUrl + "teaching/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "招生就业":
                        serverUrl = baseUrl + "recruitment/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "交流合作":
                        serverUrl = baseUrl + "cooperation/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "校园生活":
                        serverUrl = baseUrl + "campus/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "媒体重大":
                        serverUrl = baseUrl + "media/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    case "通知公告简报":
                        serverUrl = baseUrl + "notification/";
                        newsArr[0] = NewsHttpUtil.commonRequest(serverUrl);
                        break;
                    default:
                        serverUrl=baseUrl;
                        NewsHttpUtil.commonRequest(serverUrl);
                        break;
                }
            }
        });
        subThread.start();
        subThread.join();
        return newsArr[0];
    }

    /*
    * the commont request for all kinds of news
    * @param: url of server
    * @return: the news list as JSONArray
    * */
    private static JSONArray commonRequest(String serverUrl) {
        JSONArray newsArr=new JSONArray();
        //connect to server
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);

            //request successfully, get the data.
            if (conn.getResponseCode() == 200) {
                Log.d("connection", "get news success");
                InputStream inStream = conn.getInputStream();
                StringBuilder strBuilder=new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    strBuilder.append(line);
                }
                //parse json: json string -> json object
                String jsonstr=strBuilder.toString();
                JSONArray jsonArray=new JSONArray(jsonstr);
                for(int i=0;i<jsonArray.length();++i){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    JSONObject obj=jsonObject.getJSONObject("fields");
                    newsArr.put(obj);
                }
                reader.close();
                conn.disconnect();
                return newsArr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("connection", e.getMessage());
        }
        return newsArr;
    }
}
