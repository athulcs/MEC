package com.enigmalabs.drawer_activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AttendanceActivity extends AppCompatActivity {

    String text;
    WebView web;
    ProgressBar pro;
    private class DownloadTask extends AsyncTask<String,Void,String> {
        String result;
        @Override
        protected void onPreExecute() {
            //Setup precondition to execute some task
            //    pro.setVisibility(View.VISIBLE);
        }



        @Override
        protected String doInBackground (String... urls){
            result="";
            URL url;
            HttpURLConnection urlconnection =null;

            try{
                url = new URL(urls[0]);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("class",text);
                postDataParams.put("submit", "view");
                Log.e("params",postDataParams.toString());

                urlconnection = (HttpURLConnection)url.openConnection();
                urlconnection.setRequestMethod("POST");
                urlconnection.setDoInput(true);
                urlconnection.setDoOutput(true);
                urlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                OutputStream os = urlconnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write("class="+text+"&submit=view");

                writer.flush();
                writer.close();
                os.close();

                int responseCode=urlconnection.getResponseCode();
                Log.i("Response:",Integer.toString(responseCode));
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlconnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result+=inputLine;
                in.close();

                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            //Show the result obtained from doInBackground
            pro.setVisibility(View.INVISIBLE);
            web.loadData(result,"text/html","UTF-8");

        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Attendance");
        setContentView(R.layout.activity_attendance);
        Intent intent=getIntent();
        text=intent.getStringExtra("class");
        DownloadTask task = new DownloadTask();
        task.execute("http://attendance.mec.ac.in/view4stud.php");
        //Log.i("Contents:",result);
        pro=(ProgressBar) findViewById(R.id.atten_progressBar);

        web = (WebView)findViewById(R.id.atten_webview);
        web.getSettings().setJavaScriptEnabled(true);
        //  web.getSettings().setSupportZoom(true);  // Adds Default zoom controls
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setBuiltInZoomControls(true); // Enables Zoom
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);

    }
}
