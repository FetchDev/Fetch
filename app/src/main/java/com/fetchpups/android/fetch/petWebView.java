package com.fetchpups.android.fetch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fetchpups.android.fetch.utils.WebChromeClientWithProgress;

public class petWebView extends Fragment {

    WebView mWebView;
    ProgressBar prgBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weblayout, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        prgBar = (ProgressBar) v.findViewById(R.id.web_progress);

        String url = getArguments().getString("url");

        //Enable javascript ---not sure if needed
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Force links and redirects to open nin the webview
        mWebView.setWebChromeClient(new WebChromeClientWithProgress(prgBar));
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);

        return v;
    }
}
