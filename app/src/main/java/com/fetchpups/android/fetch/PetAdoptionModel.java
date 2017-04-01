package com.fetchpups.android.fetch;

/**
 * Created by Eduardo on 3/31/2017.
 */

public class PetAdoptionModel {
    private String mPetImageUrl;
    private String mPetName;
    private String mSrcUrl;
    //Possibly another field for description

    public PetAdoptionModel(String petImageUrl, String petName, String srcUrl) {
        mPetImageUrl    = petImageUrl;
        mPetName        = petName;
        mSrcUrl         = srcUrl;
    }

    public String getPetImageUrl() {
        return mPetImageUrl;
    }

    public String getPetName() {
        return mPetName;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }
}
