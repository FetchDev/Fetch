package com.fetchpups.android.fetch.controllers;

/**
 * Created by micgl on 4/5/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fetchpups.android.fetch.EventWebView;
import com.fetchpups.android.fetch.R;
import com.fetchpups.android.fetch.models.LocalEventModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocalEventAdapter extends ArrayAdapter<LocalEventModel>{

    private static class ViewHolder {
        TextView eventTime;
        TextView eventTitle;
        TextView eventDate;
        TextView location;
        ImageView imgUrl;
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LocalEventModel> mDataset;

    public LocalEventAdapter(Context context, ArrayList<LocalEventModel> dataset) {
        super(context, R.layout.event_list_item_view, dataset);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDataset = dataset;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LocalEventModel currentEvent = getItem(position);
        LocalEventAdapter.ViewHolder viewHolder;


        /*
        *   This conditional is boilerplate for a custom adapter's getView() function
         */

        //Check if existing view is being reused, else inflate the view
        if(convertView == null){
            viewHolder = new LocalEventAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.event_list_item_view, parent, false);
            viewHolder.eventTime = (TextView) convertView.findViewById(R.id.item_event_time);
            viewHolder.eventDate = (TextView) convertView.findViewById(R.id.item_event_date);
            viewHolder.location = (TextView) convertView.findViewById(R.id.item_event_location);
            viewHolder.eventTitle = (TextView) convertView.findViewById(R.id.item_event_title);
            viewHolder.imgUrl = (ImageView) convertView.findViewById(R.id.item_img_url);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //Set the corresponding model fields to their respective views in the item view
        viewHolder.eventTime.setText(currentEvent.getEventTime());
        viewHolder.eventDate.setText(currentEvent.getEventDate());
        viewHolder.eventTitle.setText(currentEvent.getEventTitle());
        viewHolder.location.setText(currentEvent.getLocation());
        //Picasso library for remote sources for ImageViews
        Picasso.with(mContext).load(currentEvent.getImgUrl()).resize(300,300).centerCrop().into(viewHolder.imgUrl);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Src: " + currentPet.getSrcUrl(), Snackbar.LENGTH_LONG).setAction("No action", null).show();

                //pass url into bundle for petWebView
                Bundle bundle = new Bundle();
                bundle.putString("url",currentEvent.getSrcUrl());

                //Create fragment and pass url into fragment
                Fragment fragment = new EventWebView();
                fragment.setArguments(bundle);

                //Display fragment
                if (getContext() instanceof FragmentActivity){
                    FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        return convertView;
    }
}



