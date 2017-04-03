package com.fetchpups.android.fetch.models;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class LocalEventModel {
    private String mEventTitle;
    private String mEventTime;      //Holds the full range in a string ("9:00am - 3:00pm"). Directly from the web site, so it probably doesn't adjust to timezones.
    private String mEventDate;      //Also directly from the website ("Mar 13, 2017")
    private String mLocation;
    private String mLocationUrl;    //Map URL that points to the location
    private String mImgUrl;
    private String mSrcUrl;         //Can't get the event's src url directly, so just use the post's self-url
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
