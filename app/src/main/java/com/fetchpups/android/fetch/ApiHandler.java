package com.fetchpups.android.fetch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
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

    private static  RecyclerView.Adapter dogListAdapter;
    private static  RecyclerView.Adapter catListAdapter;
    private static  RecyclerView.Adapter eventListAdapter;



    private static String getCityPrefApiUrl(Context mContext, int selection) {
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

        //TODO: Remove this if I decide not to use apiUrl
        String apiUrl = cityPrefUrl + "?format=json-pretty";
        Log.d("Api.getCityPrefApiUrl", apiUrl);

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

    public static void updateDogAdoptionList(Context context, RecyclerView.Adapter listAdapter, List<PetAdoptionModel> petList){
        String apiUrl = getCityPrefApiUrl(context, 1);

        dogList = petList;
        dogListAdapter = listAdapter;
        sendDogListRequest(apiUrl, context);
    }

    //TODO: Revert to private after testing done
    public static void sendDogListRequest(String url, Context context){
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("stringRequest", "Response preview: " + response.substring(0, 20));

                        //TODO: Parse the entire HTML instead of the HTML from the JSON response (Has weird escape characters and char encoding)
//                        dogList.clear();
//                        //Rebuild the list etc etc
                        parseDogHtml(response);
//                        dogListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("stringRequest", "Error while fetching JSON");
                    }
                });

        mRequestQueue.add(stringRequest);
    }

    //TODO: Change this back to private when done testing
    public static void parseDogHtml(String html){
        //Modify the dogList within here
        Document doc = Jsoup.parse(html);
        Element body = doc.body();

        //All posts are nested within the divs with class intrinsic
        Elements petPosts = body.getElementsByClass("intrinsic");

        String parserEmptyBool = String.valueOf(petPosts.isEmpty());
        Log.d("parseDogHtml", "Is parser empty: " + parserEmptyBool);

        //Generate the PetAdoptionModel objects & add to List<> within this loop
        for (Element post : petPosts){
            String mSrcUrl, mPetName, mPetImgUrl;

            //mSrcUrl
            mSrcUrl = post.getElementsByTag("a").attr("href");
            Log.d("parseDogHtml", "mSrcUrl: " + mSrcUrl);

            //mPetName
            mPetName = post.getElementsByTag("em").text();
            Log.d("parseDogHtml", "mPetName: " + mPetName);

            //mPetImgUrl
            mPetImgUrl = post.getElementsByTag("img").attr("src");
            Log.d("parseDogHtml", "mPetImgUrl: " + mPetImgUrl);

            PetAdoptionModel dog = new PetAdoptionModel(mPetImgUrl, mPetName, mSrcUrl);
            dogList.add(dog);
        }

    }

    public static ArrayList<PetAdoptionModel> getCatAdoptionList(Context context){
        String apiUrl = getCityPrefApiUrl(context, 2);
        return null;
    }


    public static ArrayList<LocalEventModel> getEventList(Context context){
        String apiUrl = getCityPrefApiUrl(context, 3);
        return null;
    }

}
