package com.appspace.evytinkadmin.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.appspace.evytinkadmin.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(view);
        return view;
    }

    private WebView wvMain;

    @SuppressLint("SetJavaScriptEnabled")
    private void initInstances(View view) {
        wvMain = (WebView) view.findViewById(R.id.wvMain);
        WebSettings webSettings = wvMain.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void loadUrl(String url) {
        wvMain.loadUrl(url);
    }
}
