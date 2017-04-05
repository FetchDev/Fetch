package com.fetchpups.android.fetch;

/**
 * Created by micgl on 4/4/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.fetchpups.android.fetch.utils.ApiHandler;
import com.fetchpups.android.fetch.utils.EnableBackNavOnWebView;
import com.fetchpups.android.fetch.utils.WebChromeClientWithProgress;

public class PetProducts extends Fragment {
    WebView mWebView;
    ProgressBar prgBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weblayout, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        prgBar = (ProgressBar) v.findViewById(R.id.web_progress);

        String pageUrl = ApiHandler.getCityPrefUrl(getActivity(), 4);

        //Enable javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Force links and redirects to open in the webview
        mWebView.setWebChromeClient(new WebChromeClientWithProgress(prgBar));
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setOnKeyListener(new EnableBackNavOnWebView());
        mWebView.loadUrl(pageUrl);

        return v;
    }
}