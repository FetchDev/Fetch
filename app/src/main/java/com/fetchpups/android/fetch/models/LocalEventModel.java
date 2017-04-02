package com.fetchpups.android.fetch.models;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class LocalEventModel {
    private String mEventTitle;
    private String mEventTime;      //Maybe two different fields for BeginTime/EndTime?
    private String mEventDate;      //Look up SimpleDateFormat or Calendar (Probably SDF, Calendar is more for calculating stuff with Dates)
    private String mLocation;       //Should this be a link to the provided Map URL, or just plain text of the location? Do both just in case
    private String mLocationUrl;    //Map URL that points to this location
    private String mImgUrl;
    private String mSrcUrl;         //Can't get the event's src url directly, so just use this for now
    private boolean mIsUpcoming;

    public LocalEventModel(String eventTitle, String eventDate, String eventTime, String location, String locationUrl, String imgUrl, String srcUrl, boolean isUpcoming) {
        mImgUrl = imgUrl;
        mEventTime = eventTime;
        mEventTitle = eventTitle;
        mEventDate = eventDate;
        mSrcUrl = srcUrl;
        mLocation = location;
        mLocationUrl = locationUrl;
        mIsUpcoming = isUpcoming;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public String getEventDate() {
        return mEventDate;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getLocationUrl() {
        return mLocationUrl;
    }

    public boolean isUpcoming() {
        return mIsUpcoming;
    }
}
