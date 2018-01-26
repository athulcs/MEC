package com.enigmalabs.drawer_activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fragment3 extends Fragment implements View.OnClickListener{

    WebView web;
    FloatingActionButton f;
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    private OnFragmentInteractionListener mListener;

    public Fragment3() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        f=(FloatingActionButton)getView().findViewById(R.id.floatingActionButton);
        f.setOnClickListener(this);
        web = (WebView)getView().findViewById(R.id.siraj_webview);
        web.setWebViewClient(new MyBrowser());
        web.loadUrl("http://14.139.184.212/ask/");
        web.getSettings().setJavaScriptEnabled(true);
        //  web.getSettings().setSupportZoom(true);  // Adds Default zoom controls
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setBuiltInZoomControls(true); // Enables Zoom
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
    }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.floatingActionButton) {
                Log.i("FAB","YAS");
                if(web.canGoBack())
                    web.goBack();
                else
                    web.loadUrl("http://14.139.184.212/ask/");
            }
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mListener != null) {
            mListener.onFragmentInteraction("Siraj");
        }
        return inflater.inflate(R.layout.fragment_fragment3, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String title);
    }
}
