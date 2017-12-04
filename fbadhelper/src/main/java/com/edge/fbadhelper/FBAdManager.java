package com.edge.fbadhelper;

import android.content.Context;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

/**
 * Created by user1 on 2017-12-01.
 */

public  class FBAdManager {
    private NativeAdsManager adsManager;
    private String placement_id;
    private Context context;
    private boolean isCaching;
    private int adLoadCount;
    private FBLoadListener fbLoadListener;
    private FBAdManager(Builder builder) {
        this.context = builder.context;
        this.placement_id = builder.placement_id;
        this.isCaching = builder.isCaching;
        this.adLoadCount = builder.adLoadCount;
        this.fbLoadListener = builder.fbLoadListener;
        setAdsManager();
    }
    public static class Builder{
        private String placement_id;
        private Context context;
        private boolean isCaching;
        private int adLoadCount;
        private FBLoadListener fbLoadListener;
        public Builder(String placement_id, Context context) {
            this.placement_id = placement_id;
            this.context = context;
        }

        public Builder isCaching(boolean isCaching){
            this.isCaching = isCaching;
            return this;
        }
        public Builder setAdLoadCount(int adLoadCount){
            this.adLoadCount = adLoadCount;
            return this;
        }
        public Builder setListener(FBLoadListener fbLoadListener){
            this.fbLoadListener = fbLoadListener;
            return this;
        }

        public FBAdManager build(){
            return new FBAdManager(this);
        }

    }
    private void setAdsManager(){

        adsManager = new NativeAdsManager(context, placement_id,adLoadCount);
        if (isCaching){
            adsManager.loadAds(NativeAd.MediaCacheFlag.ALL);
        } else {
            adsManager.loadAds();
        }
        adsManager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                if (fbLoadListener!=null){
                    fbLoadListener.onLoadSuccess(adsManager);
                }
            }
            @Override
            public void onAdError(AdError adError) {
                if (fbLoadListener!=null){
                    fbLoadListener.onLoadFail(adError);
                }
            }
        });
    }

    public NativeAdsManager getAdsManager() {
        return adsManager;
    }



}
