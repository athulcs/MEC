package com.enigmalabs.drawer_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Attenreal extends AppCompatActivity {
    String classdiv, rollno;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attenreal);
        Intent intent = getIntent();
        classdiv = intent.getStringExtra("class");
        rollno = intent.getStringExtra("rollno");
        web = (WebView)findViewById(R.id.attenreal_wv);
        web.getSettings().setJavaScriptEnabled(true);
        //  web.getSettings().setSupportZoom(true);  // Adds Default zoom controls
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setBuiltInZoomControls(true); // Enables Zoom
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        loadPage();
    }

    void loadPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
             String result=null;
        try {
            Document doc = Jsoup.connect("http://attendance.mec.ac.in/view4stud.php")
                    .data("class",classdiv)
                    .data("submit", "view")
// and other hidden fields which are being passed in post request.
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post();
            result=doc.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

                final String finalResult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        web.loadData(finalResult,"text/html","UTF-8");
                    }
                });
            }
        }).start();
    }
}
