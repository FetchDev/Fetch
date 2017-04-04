package com.fetchpups.android.fetch.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fetchpups.android.fetch.R;
import com.fetchpups.android.fetch.models.PetAdoptionModel;
import com.fetchpups.android.fetch.petWebView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Eduardo on 4/1/2017.
 */

public class PetAdoptionAdapter extends ArrayAdapter<PetAdoptionModel> {

    //View lookup cache
    private static class ViewHolder {
        TextView petName;
        TextView petDesc;
        ImageView petImg;
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<PetAdoptionModel> mDataset;

    public PetAdoptionAdapter(Context context, ArrayList<PetAdoptionModel> dataset) {
        super(context, R.layout.example_pet_item_view, dataset);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataset = dataset;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PetAdoptionModel currentPet = getItem(position);
        ViewHolder viewHolder;


        /*
        *   This conditional is boilerplate for a custom adapter's getView() function
         */

        //Check if existing view is being reused, else inflate the view
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.example_pet_item_view, parent, false);
            viewHolder.petName = (TextView) convertView.findViewById(R.id.item_pet_name);
            viewHolder.petDesc = (TextView) convertView.findViewById(R.id.item_pet_description);
            viewHolder.petImg = (ImageView) convertView.findViewById(R.id.item_pet_img);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //Set the corresponding model fields to their respective views in the item view
        viewHolder.petName.setText(currentPet.getPetName());
        viewHolder.petDesc.setText(currentPet.getPetDesc());

        //Picasso library for remote sources for ImageViews
        Picasso.with(mContext).load(currentPet.getPetImgUrl()).resize(300,300).centerCrop().into(viewHolder.petImg);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Src: " + currentPet.getSrcUrl(), Snackbar.LENGTH_LONG).setAction("No action", null).show();

                //pass url into bundle for petWebView
                Bundle bundle = new Bundle();
                bundle.putString("url",currentPet.getSrcUrl());

                //Create fragment and pass url into fragment
                Fragment fragment = new petWebView();
                fragment.setArguments(bundle);

                //Display fragment
                if (getContext() instanceof FragmentActivity){
                    FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame,fragment);
                    ft.commit();
                }
                //TODO: back button exits out of app, need to just close fragment
            }
        });
        return convertView;
    }
}
