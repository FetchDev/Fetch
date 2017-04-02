package com.fetchpups.android.fetch.models;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class PetAdoptionModel {
    private String mPetImgUrl;
    private String mPetName;
    private String mSrcUrl;
    private String mPetDesc;

    public PetAdoptionModel(String petImgUrl, String petName, String srcUrl, String petDesc) {
        mPetImgUrl      = petImgUrl;
        mPetName        = petName;
        mSrcUrl         = srcUrl;
        mPetDesc        = petDesc;
    }

    public String getPetImgUrl() {
        return mPetImgUrl;
    }

    //Possibly another field for description
    public String getPetDesc() {
        return mPetDesc;
    }

    public String getPetName() {
        return mPetName;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }
}
