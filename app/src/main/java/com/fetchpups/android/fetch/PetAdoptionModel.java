package com.fetchpups.android.fetch;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class PetAdoptionModel {
    private String mPetImgUrl;
    private String mPetName;
    private String mSrcUrl;
    //Possibly another field for description

    public PetAdoptionModel(String petImgUrl, String petName, String srcUrl) {
        mPetImgUrl      = petImgUrl;
        mPetName        = petName;
        mSrcUrl         = srcUrl;
    }

    public String getPetImgUrl() {
        return mPetImgUrl;
    }

    public String getPetName() {
        return mPetName;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }
}
