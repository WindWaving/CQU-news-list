/**
 * show details of news
 * this is releted to news_detail.xml
 */
package com.news.cqunews;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetail extends AppCompatActivity {
    //params transferred
    String title;
    String author;
    String date;
    String digest;
    String content;
    String label;
    //controls
    TextView tv_title;
    TextView tv_author;
    TextView tv_date;
    TextView tv_digest;
    TextView tv_content;
    TextView tv_label;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        author=intent.getStringExtra("author");
        date=intent.getStringExtra("date");
        digest=intent.getStringExtra("digest");
        content=intent.getStringExtra("content");
        label=intent.getStringExtra("label");
        tv_title=findViewById(R.id.news_title);
        tv_author=findViewById(R.id.news_author);
        tv_date=findViewById(R.id.news_date);
        tv_digest=findViewById(R.id.news_digest);
        tv_content=findViewById(R.id.news_content);
        tv_label=findViewById(R.id.news_label);

        setView();
    }

    /*
    * set view
    * */
    private void setView(){
        tv_title.setText(title);
        tv_author.setText(author);
        tv_date.setText(date);
        tv_digest.setText("\u3000\u3000"+digest);
        tv_content.setText("\u3000\u3000"+content);
        tv_label.setText(label);
    }

    /*
    * left top corner:back button
    * */

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//show the back button
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("back");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)  //应用程序图标的id
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
