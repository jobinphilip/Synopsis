package com.synopsis.androidapp.synopsis;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Kumar on 9/8/2016.
 */
public class Chat_web_view_fragment extends Fragment {
    WebView chat_WebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_web_view, container, false);
        chat_WebView = (WebView) view.findViewById(R.id.chat_WebView);
        chat_WebView.getSettings().setJavaScriptEnabled(true);
        chat_WebView.loadUrl("https://chatserver5.comm100.com/ChatWindow.aspx?siteId=107734&planId=2079&visitType=1&byHref=1&partnerId=-1");
        chat_WebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        return view;


    }

}
