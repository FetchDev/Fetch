package com.fetchpups.android.fetch;

/**
 * Created by micgl on 4/5/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fetchpups.android.fetch.controllers.PetAdoptionAdapter;
import com.fetchpups.android.fetch.models.PetAdoptionModel;
import com.fetchpups.android.fetch.utils.ApiHandler;

import java.util.ArrayList;

public class CatListView extends Fragment {
    ArrayList<PetAdoptionModel> kittyList;
    PetAdoptionAdapter adapter;

    public CatListView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.pet_list_view, container, false);

        //Initialize an empty list of the corresponding model
        kittyList = new ArrayList<>();

        //Standard ListView + Custom adapters boilerplate
        ListView demoListView = (ListView) rootView.findViewById(R.id.adoption_list_view);
        adapter = new PetAdoptionAdapter(getActivity(), kittyList);
        demoListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        //Use the ApiHandler.update__ (whatever corresponds to the model) after setting the adapter to the list view

        //Calling this in onResume will allow the screen to refresh on preference changes as well
        ApiHandler.updateCatAdoptionList(getActivity(), adapter, kittyList);
        super.onResume();
    }

}