package com.fetchpups.android.fetch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

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

    private static String getCityPrefApiUrl(Context mContext, int selection) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        int cityPref = prefs.getInt("major_city_preference", 1);
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

        String apiUrl = cityPrefUrl + "?format=json-pretty";
        Log.d("Api.getCityPrefApiUrl", apiUrl);

        return apiUrl;
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

    public static ArrayList<PetAdoptionModel> getDogAdoptionList(Context context){
        String apiUrl = getCityPrefApiUrl(context, 1);
        return null;
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
