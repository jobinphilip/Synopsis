package com.synopsis.androidapp.synopsis;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Kumar on 9/8/2016.
 */
public class Chat_web_view_fragment extends Fragment {
    WebView chat_WebView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_web_view, container, false);
        chat_WebView = (WebView) view.findViewById(R.id.chat_WebView);

        getActivity().getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        progressBar = (ProgressBar) view.findViewById(R.id.chat_progressbar);
       // getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

        chat_WebView.setWebViewClient(new myWebClient());

        chat_WebView.getSettings().setJavaScriptEnabled(true);
        chat_WebView.loadUrl("https://chatserver5.comm100.com/ChatWindow.aspx?siteId=107734&planId=2079&visitType=1&byHref=1&partnerId=-1");

        return view;


    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }
    }

}
