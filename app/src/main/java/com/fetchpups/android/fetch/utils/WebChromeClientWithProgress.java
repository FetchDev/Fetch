package com.fetchpups.android.fetch.utils;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Eduardo on 4/5/2017.
 */

public class WebChromeClientWithProgress extends WebChromeClient {
    ProgressBar mProgressBar;

    public WebChromeClientWithProgress(ProgressBar progBar) {
        super();
        mProgressBar = progBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mProgressBar.setProgress(newProgress);

        if(newProgress == 100){
            mProgressBar.setVisibility(View.GONE);
        } else{
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
