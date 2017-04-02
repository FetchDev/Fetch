package com.fetchpups.android.fetch;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
        View rootView = inflater.inflate(R.layout.adoption_list_view, container, false);

        ArrayList<PetAdoptionModel> demoDogList = new ArrayList<>();

        ListView demoListView = (ListView) rootView.findViewById(R.id.adoption_list_view);
        PetAdoptionAdapter adapter = new PetAdoptionAdapter(getActivity(), demoDogList);
        demoListView.setAdapter(adapter);

        ApiHandler.updateDogAdoptionList(getActivity(), adapter, demoDogList);

        return rootView;
    }

}
