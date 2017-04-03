package com.fetchpups.android.fetch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fetchpups.android.fetch.utils.ApiHandler;

public class News extends Fragment {

    //TODO: use variable to differentiate between local areas or national - default Tampa
    String area = "tampa";
    WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        String pageUrl = ApiHandler.getCityPrefUrl(getActivity(), 5);

//        switch (area){
//            case "st_Pete":
//                mWebView.loadUrl("https://www.fetchpups.com/local-scoop-1/");
//                break;
//            case "clearwater":
//                mWebView.loadUrl("https://www.fetchpups.com/local-scoop-2/");
//                break;
//            case "orlando":
//                mWebView.loadUrl("https://www.fetchpups.com/local-scoop-3/");
//                break;
//            case "tampa":
//            default:
//                mWebView.loadUrl("https://www.fetchpups.com/local-scoop/");
//                break;
//        }


        //Enable javascript ---not sure if needed
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Force links and redirects to open nin the webview
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(pageUrl);

        return v;
    }
}
