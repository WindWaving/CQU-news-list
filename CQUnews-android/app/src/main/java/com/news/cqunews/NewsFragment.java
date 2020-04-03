package com.news.cqunews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {

    //listview params
    ListView newsList;
    private String[]list_components;//names of components
    private int[] list_compo_ids;//ids of components
    private JSONArray newsArr;// news list
    public static final String SERVICE_RECEIVER="com.cqunews.UPDATE_RECEIVER";
    private View rootview;
    ResRecevier resRecevier;

    //broadcast receiver to update listview
    private class ResRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            // re-render listview
            newsArr=GetGlobals.NEWS_ARRAY;
            initData(1);
        }
    }

    /**
     * register receiver
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //register receiver to accept update data
        resRecevier= new ResRecevier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERVICE_RECEIVER);
        getActivity().registerReceiver(resRecevier,intentFilter);
    }

    /**
     * unregister receiver
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(resRecevier);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if the fragment has been loaded,if so, don't reload again.
        if(rootview==null){
            rootview= inflater.inflate(R.layout.news_fragment,container,false);
            newsList=rootview.findViewById(R.id.newslist);
            newsArr=null;
            //get parameters
            if(getArguments()!=null){
                String label=getArguments().getString("label");
                System.out.println(label);
                try {
                    // net is required
                    ConnectivityManager manager=(ConnectivityManager)getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(manager!=null) {
                        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                        if (networkInfo == null || !networkInfo.isAvailable()) {
                            Toast.makeText(getContext(), "Please connect to network", Toast.LENGTH_LONG).show();
                        } else {
                            newsArr = NewsHttpUtil.reqNews(label);
                            initData(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            ViewGroup parent=(ViewGroup)rootview.getParent();
            if(parent!=null){
                parent.removeView(rootview);
            }
            System.out.println(newsArr);
        }

        return rootview;
    }

    /*
    * init data for listview
    * @param: label of this fragment
    *       init: if the page is updated,if is, re-find listview in this page.
    * */
    private void initData(int init){
        if(init==1){
            newsList=getActivity().findViewById(R.id.newslist);
        }
        int news_cnt=newsArr.length();

        // params to transfer in initNewslist
        List<String> titles=new ArrayList<String>();
        List<String> authors=new ArrayList<String>();
        List<String> dates=new ArrayList<String>();
        try{
            for (int i=0;i<news_cnt;++i){
                titles.add(newsArr.getJSONObject(i).getString("title"));
                authors.add(newsArr.getJSONObject(i).getString("author"));
                dates.add(newsArr.getJSONObject(i).getString("date"));
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("newslist",e.getMessage());
        }

        initNewsList(titles.toArray(new String[titles.size()]), authors.toArray(new String[authors.size()]), dates.toArray(new String[dates.size()]));
    }

    /*
     * init listview
     * @params:
     *      
     * */
    private void initNewsList(final String[]title, String[]author, String[]date){
        list_components=new String[]{"mtitle","author","date"};
        list_compo_ids=new int[]{R.id.list_title,R.id.list_author,R.id.list_date};
        ArrayList<HashMap<String,Object>> listItem=new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<title.length;++i){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("mtitle",title[i]);
            map.put("author",author[i]);
            map.put("date",date[i]);
            listItem.add(map);
        }
        SimpleAdapter mAdapter=new SimpleAdapter(getContext(),listItem,R.layout.single_news,list_components,list_compo_ids);
        newsList.setAdapter(mAdapter);

        //event:click on the news to the newsDetail page
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getContext(),NewsDetail.class);
                //transfer params
                try {
                    String title=newsArr.getJSONObject(position).getString("title");
                    String author=newsArr.getJSONObject(position).getString("author");
                    String date=newsArr.getJSONObject(position).getString("date");
                    String digest=newsArr.getJSONObject(position).getString("digest");
                    String content=newsArr.getJSONObject(position).getString("content");
                    String label=newsArr.getJSONObject(position).getString("label");
                    if(date=="0"){
                        date="";
                    }
                    intent.putExtra("title",title);
                    intent.putExtra("author",author);
                    intent.putExtra("date",date);
                    intent.putExtra("digest",digest);
                    intent.putExtra("content",content);
                    intent.putExtra("label",label);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("intent putextra",e.getMessage());
                }
            }
        });
    }

    /*
    * get new instance of the class NewsFragment
    * @param: the label of news
    * @return: the fragment layout.
    * */
    public static NewsFragment Instance(String label){
        Bundle args=new Bundle();
        NewsFragment fragment=new NewsFragment();
        //transfer parameters to fragment
        args.putString("label",label);
        fragment.setArguments(args);
        return fragment;
    }
}
