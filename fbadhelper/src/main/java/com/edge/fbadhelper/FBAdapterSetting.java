package com.edge.fbadhelper;

import com.facebook.ads.NativeAdsManager;

/**
 * Created by user1 on 2017-12-04.
 */

public class FBAdapterSetting {

    private  int adInterval=10;
    private NativeAdsManager mAds;

    private FBAdapterSetting(Builder builder) {
        this.adInterval = builder.adInterval;
        this.mAds = builder.mAds;
    }

    public static class Builder {

        private  int adInterval=10;
        NativeAdsManager mAds;


        public Builder setAdInterval(int adInterval){
            this.adInterval = adInterval;
            return this;
        }

        public Builder setAdsManager(NativeAdsManager nativeAdsManager){
            this.mAds = nativeAdsManager;
            return this;
        }
        public FBAdapterSetting build(){
            return new FBAdapterSetting(this);
        }
    }

    public int getAdInterval() {
        return adInterval;
    }

    public NativeAdsManager getmAds() {
        return mAds;
    }
}
