package com.fetchpups.android.fetch;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class TestListViewFragment extends Fragment {


    public TestListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.example_adoption_list_view, container, false);

        //Initialize an empty list of the corresponding model
        ArrayList<PetAdoptionModel> demoList = new ArrayList<>();

        //Standard ListView + Custom adapters boilerplate
        ListView demoListView = (ListView) rootView.findViewById(R.id.adoption_list_view);
        PetAdoptionAdapter adapter = new PetAdoptionAdapter(getActivity(), demoList);
        demoListView.setAdapter(adapter);

        //Use the ApiHandler.update__ (whatever corresponds to the model) after setting the adapter to the list view
        ApiHandler.updateDogAdoptionList(getActivity(), adapter, demoList);

        return rootView;
    }

}
