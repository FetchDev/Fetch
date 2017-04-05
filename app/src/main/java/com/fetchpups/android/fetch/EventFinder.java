package com.fetchpups.android.fetch;


/**
 * Created by micgl on 4/5/2017.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by micgl on 4/5/2017.
 */
import android.widget.ListView;

import com.fetchpups.android.fetch.controllers.LocalEventAdapter;
import com.fetchpups.android.fetch.models.LocalEventModel;
import com.fetchpups.android.fetch.utils.ApiHandler;

import java.util.ArrayList;

public class EventFinder extends Fragment {
    ArrayList<LocalEventModel> eventList;
    LocalEventAdapter adapter;

    public EventFinder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.local_event_view, container, false);

        //Initialize an empty list of the corresponding model
        eventList = new ArrayList<>();

        //Standard ListView + Custom adapters boilerplate
        ListView demoListView = (ListView) rootView.findViewById(R.id.local_event_list_view);
        adapter = new LocalEventAdapter(getActivity(), eventList);
        demoListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        //Use the ApiHandler.update__ (whatever corresponds to the model) after setting the adapter to the list view

        //Calling this in onResume will allow the screen to refresh on preference changes as well
        ApiHandler.updateLocalEventList(getActivity(), adapter, eventList);
        super.onResume();
    }
}