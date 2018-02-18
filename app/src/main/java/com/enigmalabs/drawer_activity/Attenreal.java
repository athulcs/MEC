package com.enigmalabs.drawer_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Console;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Attenreal extends AppCompatActivity {
    String classdiv, rollno;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Attendance");
        setContentView(R.layout.activity_attenreal);
        Intent intent = getIntent();
        classdiv = intent.getStringExtra("class");
        rollno = intent.getStringExtra("rollno");
        text=(TextView)findViewById(R.id.attenreal_tv);
        text.setMovementMethod(new ScrollingMovementMethod());
        loadPage();
    }

    void loadPage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
             String result="";
        try {
            Document doc = Jsoup.connect("http://attendance.mec.ac.in/view4stud.php")
                    .data("class",classdiv)
                    .data("submit", "view")
                    //.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .get();
            float bunked;
            float percent=0;
            Element table = doc.select("table").get(0);
            Elements rows = table.select("tr");
            Element row=rows.get(Integer.parseInt(rollno)+1);
            Elements cols = row.select("td");
            Element headrow=rows.get(0);
            Elements schema=headrow.select("td");
            Pattern p=Pattern.compile("\\((.*?)\\)");
            for(int i=0;i<cols.size();i++) {
                result+=schema.get(i).text() +"--";
                result+= cols.get(i).text()+" ";
                if(i>1&&i<11) {
                    Matcher m = p.matcher(schema.get(i).text());
                    while (m.find()) {
                        percent = 100 - Float.valueOf(cols.get(i).text());
                        bunked = (Float.valueOf(m.group(1)) * percent) / 100;
                        //Log.i("Bunked",String.valueOf(Math.round(bunked)));
                        result+="Bunked="+String.valueOf(Math.round(bunked))+"\n";
                    }
                }
                else
                    result+="\n";
            }
            result+="\n";
            table=doc.select("table").get(1);
            rows=table.select("tr");
            for(int i=0;i<rows.size();i++) {
                row = rows.get(i);
                cols = row.select("td");
                for (int j = 0; j < cols.size(); j++)
                    result += cols.get(j).text()+" ";
                result+="\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

                final String finalResult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(finalResult);
                    }
                });
            }
        }).start();
    }
}
