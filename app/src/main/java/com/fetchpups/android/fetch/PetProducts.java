package com.fetchpups.android.fetch;

/**
 * Created by micgl on 4/4/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ListView;

import com.fetchpups.android.fetch.R;
import com.fetchpups.android.fetch.utils.ApiHandler;

public class PetProducts extends Fragment {
    WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weblayout, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        String pageUrl = ApiHandler.getCityPrefUrl(getActivity(), 4);

        //Enable javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Force links and redirects to open in the webview
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(pageUrl);

        return v;
    }
}