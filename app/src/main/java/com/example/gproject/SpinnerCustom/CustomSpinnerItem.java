package com.example.gproject.SpinnerCustom;

public class CustomSpinnerItem {
    private String mProviderName;
    private int mProviderImage;

    public CustomSpinnerItem(String mProviderName, int mProviderImage) {
        this.mProviderName = mProviderName;
        this.mProviderImage = mProviderImage;
    }

    public String getProviderName() {
        return mProviderName;
    }

    public int getProviderImage() {
        return mProviderImage;
    }
}
