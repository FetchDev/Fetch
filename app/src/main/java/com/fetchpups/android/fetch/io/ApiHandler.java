package com.fetchpups.android.fetch.io;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
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
 * Created by Eduardo on 3/31/2017.
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

    private static List<PetAdoptionModel> dogList;
    private static List<PetAdoptionModel> catList;
    private static List<LocalEventModel> eventList;

    private static  ArrayAdapter<PetAdoptionModel> dogListAdapter;
    private static  ArrayAdapter<PetAdoptionModel> catListAdapter;
    private static  ArrayAdapter<LocalEventModel> eventListAdapter;


    /*
    *   These methods are self-explanatory. They generate the appropriate page url based on the user's selected city preference
     */
    public static String getCityPrefApiUrl(Context mContext, int selection) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        int cityPref = Integer.parseInt(prefs.getString("major_city_preference", "1"));
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
//            case 4:             // 4 = Maybe Sales Pages
//
        }

//        TODO: Remove this if I decide not to use apiUrl
//        String apiUrl = cityPrefUrl + "?format=json-pretty";
        Log.d("Api.getCityPrefApiUrl", cityPrefUrl);

        return cityPrefUrl;
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

    /*
    *   The following methods are used to update the supplied adapters with the remote information from the dog adoption pages.
     */
    public static void updateDogAdoptionList(Context context, ArrayAdapter<PetAdoptionModel> listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefApiUrl(context, 1);

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

            PetAdoptionModel dog = new PetAdoptionModel(mPetImgUrl, mPetName, mSrcUrl, petDesc);
            dogList.add(dog);
        }

    }


    /*
    *   Utility functions for cat pages.
     */

    public static void updateCatAdoptionList(Context context, ArrayAdapter<PetAdoptionModel> listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefApiUrl(context, 2);

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

            PetAdoptionModel cat = new PetAdoptionModel(mPetImgUrl, mPetName, mSrcUrl, petDesc);
            catList.add(cat);
        }
    }



    /*
    *   Utility functions for updating event list adapters
     */
    //TODO: Change this to respective EventListModel when the time comes
    public static void updateEventList(Context context, ArrayAdapter<PetAdoptionModel> listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefApiUrl(context, 3);
    }

}
