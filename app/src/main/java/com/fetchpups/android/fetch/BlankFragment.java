package com.fetchpups.android.fetch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fetchpups.android.fetch.utils.ApiHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        Log.d("BlankFragment", "Test");
        ApiHandler.sendEventListRequest("https://www.fetchpups.com/events-1/", getActivity());

        return rootView;
    }

}
