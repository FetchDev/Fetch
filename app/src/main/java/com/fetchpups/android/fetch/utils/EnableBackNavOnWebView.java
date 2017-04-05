package com.fetchpups.android.fetch.utils;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Eduardo on 4/5/2017.
 */

public class EnableBackNavOnWebView implements View.OnKeyListener {

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
}
