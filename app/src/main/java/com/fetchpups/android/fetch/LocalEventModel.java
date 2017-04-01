package com.fetchpups.android.fetch;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class LocalEventModel {
    private String mEventTime; //Maybe two different fields for BeginTime/EndTime?
    private String mImageUrl;
    private String mEventTitle;
    private String mEventDate; //Look up SimpleDateFormat or Calendar (Probably SDF, Calendar is more for calculating stuff with Dates)

    public LocalEventModel(String eventTime, String imageUrl, String eventTitle, String eventDate) {
        mImageUrl = imageUrl;
        mEventTime = eventTime;
        mEventTitle = eventTitle;
        mEventDate = eventDate;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }


    private String mSrcUrl;     //Just point to the event's post on the website since the actual source url requires visiting that page


    public String getImageUrl() {
        return mImageUrl;
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
}
