package com.fetchpups.android.fetch;

/**
 * Created by micgl on 4/4/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    WebView wView = (WebView) v;

                    switch (keyCode){
                        case KeyEvent.KEYCODE_BACK:
                            if(wView.canGoBack()){
                                wView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
        mWebView.loadUrl(pageUrl);

        return v;
    }
}