package com.fetchpups.android.fetch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fetchpups.android.fetch.models.LocalEventModel;
import com.fetchpups.android.fetch.models.PetAdoptionModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 *  The following are the utility functions that are available
 *  {@link #getCityPrefUrl(Context, int)}
 *  {@link #updateCatAdoptionList(Context, ArrayAdapter, List)}
 *  {@link #updateDogAdoptionList(Context, ArrayAdapter, List)}
 *  {@link #updateLocalEventList(Context, ArrayAdapter, List)}
 *
 *  Check {@link com.fetchpups.android.fetch.TestListViewFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
 *  for an example using the updateList functions
 */
public final class ApiHandler {

    private static final String DOG_TAMPA_URL = "https://www.fetchpups.com/new-page-84/";
    private static final String DOG_STPETE_URL = "https://www.fetchpups.com/dog-adoptions/";
    private static final String DOG_CLEARWATER_URL = "https://www.fetchpups.com/new-page-88/";
    private static final String DOG_ORLANDO_URL = "https://www.fetchpups.com/dog-adoptions-1/";

    private static final String CAT_TAMPA_URL = "https://www.fetchpups.com/cat-adoptions-1/";
    private static final String CAT_STPETE_URL = "https://www.fetchpups.com/cat-adoptions/";
    private static final String CAT_CLEARWATER_URL = "https://www.fetchpups.com/cat-adoptions-2/";
    private static final String CAT_ORLANDO_URL = "https://www.fetchpups.com/cat-adoptions-3/";

    private static final String EVENT_TAMPA_URL = "https://www.fetchpups.com/new-events-1/";
    private static final String EVENT_STPETE_URL = "https://www.fetchpups.com/events/";
    private static final String EVENT_CLEARWATER_URL = "https://www.fetchpups.com/events-1/";
    private static final String EVENT_ORLANDO_URL = "https://www.fetchpups.com/events-2/";

    private static final String SALES_TAMPA_URL = "https://www.fetchpups.com/new-page-62/";
    private static final String SALES_STPETE_URL = "https://www.fetchpups.com/sales/";
    private static final String SALES_CLEARWATER_URL = "https://www.fetchpups.com/sales-1/";
    private static final String SALES_ORLANDO_URL = "https://www.fetchpups.com/sales-2/";

    private static final String NEWS_TAMPA_URL = "https://www.fetchpups.com/local-scoop/";
    private static final String NEWS_STPETE_URL = "https://www.fetchpups.com/local-scoop-1/";
    private static final String NEWS_CLEARWATER_URL = "https://www.fetchpups.com/local-scoop-2/";
    private static final String NEWS_ORLANDO_URL = "https://www.fetchpups.com/local-scoop-3/";

    private static final String SOCIAL_TAMPA_URL = "https://www.fetchpups.com/fetch-social/";
    private static final String SOCIAL_STPETE_URL = "https://www.fetchpups.com/fetch-social-1/";
    private static final String SOCIAL_CLEARWATER_URL = "https://www.fetchpups.com/fetch-social-2/";
    private static final String SOCIAL_ORLANDO_URL = "https://www.fetchpups.com/fetch-social-3/";

    private static final String NATIONAL_SCOOP_URL = "https://www.fetchpups.com/national-scoop-3/";

    private static List<PetAdoptionModel> dogList;
    private static List<PetAdoptionModel> catList;
    private static List<LocalEventModel> eventList;

    private static  ArrayAdapter<PetAdoptionModel> dogListAdapter;
    private static  ArrayAdapter<PetAdoptionModel> catListAdapter;
    private static  ArrayAdapter<LocalEventModel> eventListAdapter;

    /**
     *  Returns the corresponding city-relevant page URL of the passed selection
     * @param mContext  The activity context that the caller belongs to. Just pass in getActivity() from your activity/fragment for this.
     * @param selection An integer flag used to select the relevant pages (Dog adoption, events, local news, etc)
     *                  1 = Dog Adoption Pages
     *                  2 = Cat Adoption Pages
     *                  3 = Local Event Pages
     *                  4 = Local Sales Pages
     *                  5 = Local News/Scoop Pages
     *                  6 = Local Social Pages
     *                  7 = National News/Scoop Pages
     * @return
     */
    public static String getCityPrefUrl(Context mContext, int selection) {

        int cityPref = getCityPref(mContext);
        String cityPrefUrl = "";

        switch (selection) {
            case 1:             // 1 = Dog Adoption Pages
                cityPrefUrl = cityPrefUrlDogHelper(cityPref);
                break;
            case 2:             // 2 = Cat Adoption Pages
                cityPrefUrl = cityPrefUrlCatHelper(cityPref);
                break;
            case 3:             // 3 = Local Event Pages
                cityPrefUrl = cityPrefUrlEventHelper(cityPref);
                break;
            case 4:             // 4 = Local Sales Pages
                cityPrefUrl = cityPrefUrlSalesHelper(cityPref);
                break;
            case 5:             // 5 = Local News Pages
                cityPrefUrl = cityPrefUrlNewsHelper(cityPref);
                break;
            case 6:             // 6 = Local Social Pages
                cityPrefUrl = cityPrefUrlSocialHelper(cityPref);
                break;
            case 7:
                cityPrefUrl = NATIONAL_SCOOP_URL;
                break;
        }

        Log.d("Api.getCityPrefUrl", cityPrefUrl);
        return cityPrefUrl;
    }

    private static int getCityPref(Context mContext){
        //Initialize the return value to default to Tampa
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        int cityPref = Integer.parseInt(prefs.getString("major_city_preference", "1"));

        return cityPref;
    }

    private static String cityPrefUrlDogHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return DOG_TAMPA_URL;
            case 2:             // 2 = St Pete
                return DOG_STPETE_URL;
            case 3:             // 3 = Clearwater
                return DOG_CLEARWATER_URL;
            case 4:             // 4 = Orlando
                return DOG_ORLANDO_URL;
            default:            //This case shouldn't be reached, but return Tampa url just in case
                return DOG_TAMPA_URL;
        }
    }

    private static String cityPrefUrlCatHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return CAT_TAMPA_URL;
            case 2:             // 2 = St Pete
                return CAT_STPETE_URL;
            case 3:             // 3 = Clearwater
                return CAT_CLEARWATER_URL;
            case 4:             // 4 = Orlando
                return CAT_ORLANDO_URL;
            default:            //This case shouldn't be reached, but return Tampa url just in case
                return CAT_TAMPA_URL;
        }
    }

    private static String cityPrefUrlEventHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return EVENT_TAMPA_URL;
            case 2:             // 2 = St Pete
                return EVENT_STPETE_URL;
            case 3:             // 3 = Clearwater
                return EVENT_CLEARWATER_URL;
            case 4:             // 4 = Orlando
                return EVENT_ORLANDO_URL;
            default:            //This case shouldn't be reached, but return Tampa url just in case
                return EVENT_TAMPA_URL;
        }
    }

    private static String cityPrefUrlNewsHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return NEWS_TAMPA_URL;
            case 2:             // 2 = St Pete
                return NEWS_STPETE_URL;
            case 3:            // 3 = Clearwater
                return NEWS_CLEARWATER_URL;
            case 4:            // 4 = Orlando
                return NEWS_ORLANDO_URL;
            default:           //This case shouldn't be reached, but return Tampa url just in case
                return NEWS_TAMPA_URL;
        }
    }

    private static String cityPrefUrlSalesHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return SALES_TAMPA_URL;
            case 2:             // 2 = St Pete
                return SALES_STPETE_URL;
            case 3:             // 3 = Clearwater
                return SALES_CLEARWATER_URL;
            case 4:             // 4 = Orlando
                return SALES_ORLANDO_URL;
            default:            //This case shouldn't be reached, but return Tampa url just in case
                return SALES_TAMPA_URL;
        }
    }

    private static String cityPrefUrlSocialHelper(int cityPref){
        switch (cityPref) {
            case 1:             // 1 = Tampa
                return SOCIAL_TAMPA_URL;
            case 2:             // 2 = St Pete
                return SOCIAL_STPETE_URL;
            case 3:             // 3 = Clearwater
                return SOCIAL_CLEARWATER_URL;
            case 4:             // 4 = Orlando
                return SOCIAL_ORLANDO_URL;
            default:            //This case shouldn't be reached, but return Tampa url just in case
                return SOCIAL_TAMPA_URL;
        }
    }

    /**
     * The following function is used to update the passed in list+adapter with the corresponding remote data
     * IMPORTANT: Initialize your adapter using an empty List before calling this function.
     * @param context       The activity context that the caller belongs to. Just pass in getActivity() from your activity/fragment for this.
     * @param listAdapter   The custom ArrayAdapter used with your list view.
     * @param petList       The list that needs to be populated with remote data. Pass in the empty List that is used with the custom adapter.
     */
    public static void updateDogAdoptionList(Context context, ArrayAdapter<PetAdoptionModel> listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefUrl(context, 1);

        dogList = petList;
        dogListAdapter = listAdapter;
        sendDogListRequest(apiUrl, context);
    }

    private static void sendDogListRequest(String url, Context context){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("stringRequest", "Response preview: " + response.substring(0, 20));

                        //Rebuild the list and then notify the adapter of this change
                        dogList.clear();
                        parseDogHtml(response);
                        dogListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("stringRequest", "Error while fetching HTML");
                    }
                });

        mRequestQueue.add(stringRequest);
    }

    private static void parseDogHtml(String html){
        //Modify the dogList within here
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        //All posts are nested within the divs with class intrinsic
        Elements petPosts = body.getElementsByClass("intrinsic");

        //Generate the PetAdoptionModel objects & add to List<> within this loop
        for (Element post : petPosts){
            String mSrcUrl, mPetName, mPetImgUrl;

            //mSrcUrl
            mSrcUrl = post.getElementsByTag("a").attr("href");
//            Log.d("parseDogHtml", "mSrcUrl: " + mSrcUrl);

            //mPetName && mPetDesc
            //Some of the posts use a non-blocking space "&nbsp;" = '\u00a0' after the first word
            String petDesc = post.getElementsByTag("p").text().replace("\u00a0", " ");
            mPetName = petDesc.substring(0, petDesc.indexOf(' '));
//            Log.d("parseDogHtml", "mPetDesc: " + petDesc);
//            Log.d("parseDogHtml", "mPetName: " + mPetName);

            //mPetImgUrl
            mPetImgUrl = post.getElementsByTag("img").attr("src");
//            Log.d("parseDogHtml", "mPetImgUrl: " + mPetImgUrl);

            dogList.add(new PetAdoptionModel(mPetImgUrl, mPetName, mSrcUrl, petDesc));
        }

    }


    /**
     * The following function is used to update the passed in list+adapter with the corresponding remote data
     * IMPORTANT: Initialize your adapter using an empty List before calling this function.
     * @param context       The activity context that the caller belongs to. Just pass in getActivity() from your activity/fragment for this.
     * @param listAdapter   The custom ArrayAdapter used with your list view.
     * @param petList       The list that needs to be populated with remote data. Pass in the empty List that is used with the custom adapter.
     */
    public static void updateCatAdoptionList(Context context, ArrayAdapter<PetAdoptionModel> listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefUrl(context, 2);

        catList = petList;
        catListAdapter = listAdapter;
        sendCatListRequest(apiUrl, context);
    }

    private static void sendCatListRequest(String url, Context context){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("stringRequest", "Response preview: " + response.substring(0, 20));

                        //Rebuild the list and then notify the adapter that the list has changed
                        catList.clear();
                        parseCatHtml(response);
                        catListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("stringRequest", "Error while fetching HTML");
                    }
                });

        mRequestQueue.add(stringRequest);
    }

    private static void parseCatHtml(String html){
        //Modify the catList within here
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        //All posts are nested within the divs with class intrinsic
        Elements petPosts = body.getElementsByClass("intrinsic");

        //Generate the PetAdoptionModel objects & add to List<> within this loop
        for (Element post : petPosts){
            String mSrcUrl, mPetName, mPetImgUrl;

            //mSrcUrl
            mSrcUrl = post.getElementsByTag("a").attr("href");

            //mPetName && mPetDesc
            //Some of the posts use a non-blocking space "&nbsp;" = '\u00a0' after the first word
            String petDesc = post.getElementsByTag("p").text().replace("\u00a0", " ");
            mPetName = petDesc.substring(0, petDesc.indexOf(' '));

            //mPetImgUrl
            mPetImgUrl = post.getElementsByTag("img").attr("src");

            catList.add(new PetAdoptionModel(mPetImgUrl, mPetName, mSrcUrl, petDesc));
        }
    }



    /**
     * The following function is used to update the passed in list+adapter with the corresponding remote data
     * IMPORTANT: Initialize your adapter using an empty List before calling this function.
     * @param context           The activity context that the caller belongs to. Just pass in getActivity() from your activity/fragment for this.
     * @param listAdapter       The custom ArrayAdapter used with your list view.
     * @param localEventList    The list that needs to be populated with remote data. Pass in the empty List that is used with the custom adapter.
     */

    public static void updateLocalEventList(Context context, ArrayAdapter<LocalEventModel> listAdapter, List<LocalEventModel> localEventList){
        String apiUrl = getCityPrefUrl(context, 3);

        eventList = localEventList;
        eventListAdapter = listAdapter;
        sendEventListRequest(apiUrl, context);
    }

    //TODO: Revert to private && uncomment list/adapter code when finished testing
    public static void sendEventListRequest(String url, Context context){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("stringRequest", "Response preview: " + response.substring(0, 20));

                        //Rebuild the list and then notify the adapter that the list has changed
//                        eventList.clear();
                        parseEventHtml(response);
//                        eventListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("stringRequest", "Error while fetching HTML");
                    }
                });

        mRequestQueue.add(stringRequest);
    }

    private static void parseEventHtml(String html){
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        //All event posts are nested within an <article> tag
        Elements eventPosts = body.getElementsByTag("article");

        for(Element eventPost : eventPosts){
            String  mEventTitle, mEventTime, mEventDate, mLocation, mLocationUrl, mImgUrl, mSrcUrl;
            boolean mIsUpcoming;

            //mEventTitle
            mEventTitle = eventPost.getElementsByClass("eventlist-title-link").text();
//            Log.d("parseEventHtml", "Event : " + mEventTitle);

            //mEventDate
            mEventDate = eventPost.getElementsByClass("event-date").text();

            //mEventTime
            if(!eventPost.getElementsByClass("event-time-12hr-start").isEmpty()){
                mEventTime = eventPost.getElementsByClass("event-time-12hr-start").text() + " - " + eventPost.getElementsByClass("event-time-12hr-end").first().text();
            } else{
                mEventTime = eventPost.select("time.event-time-12hr").first().text() + " - " + eventPost.select("time.event-time-12hr").last().text();
            }

            //mLocation
            mLocation = eventPost.getElementsByClass("eventlist-meta-address").text();

            //mLocationUrl
            mLocationUrl = eventPost.getElementsByClass("eventlist-meta-address").first().getElementsByClass("eventlist-meta-address-maplink").attr("href");

            //mImgUrl
            mImgUrl = eventPost.getElementsByClass("eventlist-thumbnail").attr("data-src");

            //mSrcUrl
            mSrcUrl = "https://www.fetchpups.com" + eventPost.getElementsByClass("eventlist-column-thumbnail").attr("href");

            //mIsUpcoming
            mIsUpcoming = !eventPost.parent().hasClass("eventlist--past");
//            String testTxt = mIsUpcoming ? "upcoming" : "done";
//            Log.d("parseEventHtml", "Event is: " + testTxt);


//            eventList.add(new LocalEventModel(mEventTitle, mEventDate, mEventTime, mLocation, mLocationUrl, mImgUrl, mSrcUrl, mIsUpcoming));
        }


    }

}
