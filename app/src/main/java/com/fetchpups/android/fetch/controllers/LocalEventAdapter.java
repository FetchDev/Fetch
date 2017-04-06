package com.fetchpups.android.fetch.controllers;

/**
 * Created by micgl on 4/5/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
        TextView eventTitle;
        TextView eventTime;
        TextView eventDate;
        TextView eventDesc;
        TextView eventPostSrc;
        TextView locationText;
        ImageView eventImg;
        ImageView eventLocNav;
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
            viewHolder.eventTitle = (TextView) convertView.findViewById(R.id.event_title);
            viewHolder.eventTime = (TextView) convertView.findViewById(R.id.event_time);
            viewHolder.eventDate = (TextView) convertView.findViewById(R.id.event_date);
            viewHolder.eventDesc = (TextView) convertView.findViewById(R.id.event_desc);
            viewHolder.locationText = (TextView) convertView.findViewById(R.id.event_location_text);
            viewHolder.eventImg = (ImageView) convertView.findViewById(R.id.event_image);
            viewHolder.eventPostSrc = (TextView) convertView.findViewById(R.id.event_post_src);
            viewHolder.eventLocNav = (ImageView) convertView.findViewById(R.id.event_location_maps);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Set the corresponding model fields to their respective views in the item view
        viewHolder.eventTime.setText(currentEvent.getEventTime());
        viewHolder.eventDate.setText(currentEvent.getEventDate());
        viewHolder.eventTitle.setText(currentEvent.getEventTitle());
        viewHolder.locationText.setText(currentEvent.getLocation());
        viewHolder.eventDesc.setText(currentEvent.getEventDesc());
        //Picasso library for remote sources for ImageViews
        Picasso.with(mContext).load(currentEvent.getImgUrl()).resize(300,300).centerCrop().into(viewHolder.eventImg);


        viewHolder.eventImg.setOnClickListener(urlRedirectClickListener(currentEvent.getSrcUrl()));
        viewHolder.eventPostSrc.setOnClickListener(urlRedirectClickListener(currentEvent.getSrcUrl()));
        viewHolder.eventLocNav.setOnClickListener(mapsRedirectClickListener(currentEvent.getLocationAddr()));

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Snackbar.make(v, "Src: " + currentPet.getSrcUrl(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
//
//                //pass url into bundle for petWebView
//                Bundle bundle = new Bundle();
//                bundle.putString("url",url);
//
//                //Create fragment and pass url into fragment
//                Fragment fragment = new EventWebView();
//                fragment.setArguments(bundle);
//
//                //TODO: Redirect to post w/ img + learn more?
//
//                //Display fragment
//                if (getContext() instanceof FragmentActivity){
//                    FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.frame,fragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//
//                //TODO: Handle map implicit intention
//            }
//        });

        return convertView;
    }

    private View.OnClickListener urlRedirectClickListener(final String url){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Img/Learn clicked ");
                //pass url into bundle for petWebView
                Bundle bundle = new Bundle();
                bundle.putString("url",url);

                //Create fragment and pass url into fragment
                Fragment fragment = new EventWebView();
                fragment.setArguments(bundle);

                //TODO: Redirect to post w/ img + learn more?

                //Display fragment
                if (getContext() instanceof FragmentActivity){
                    FragmentTransaction ft = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame,fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        };
    }

    private View.OnClickListener mapsRedirectClickListener(final String addr){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("mapsListener", addr);
                String url = "http://maps.google.com/maps?daddr=" + addr;
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                mContext.startActivity(intent);
            }
        };
    }
}



