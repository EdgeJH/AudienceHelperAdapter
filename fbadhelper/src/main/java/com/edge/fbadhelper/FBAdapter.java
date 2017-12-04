package com.edge.fbadhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2017-12-01.
 */

public abstract class FBAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    ArrayList arrayList;
    Context context;
    private final int POST_TYPE = 1;
    private final int AD_TYPE = 2;
    private  int adInterval=10;

    private NativeAdsManager mAds;
    private NativeAd mAd;
    private FBAdapterSetting setting;
    public FBAdapter(Context context,ArrayList arrayList,FBAdapterSetting setting ) {
        this.context = context;
        this.arrayList = arrayList;
        this.setting =setting;
        this.adInterval = setting.getAdInterval();
        this.mAds = setting.getmAds();
        setArrayList();
    }

    private void setArrayList(){
        for (int i =0; i<arrayList.size(); i++){
            if ((i%adInterval)==0&i!=0){
                arrayList.add(i,null);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            View inflatedView = LayoutInflater.from(context)
                    .inflate(R.layout.fb_audience_layout, parent, false);
            return new AdHolder(inflatedView);
        }
        else {
                return onFBCreateViewHolder(parent) ;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case AD_TYPE:
                if (mAds != null && mAds.isLoaded()) {
                    mAd = mAds.nextNativeAd();
                    ((AdHolder)holder).bindView(mAd);
                }
                else {
                    ((AdHolder)holder).bindView(null);
                }
                break;
            case POST_TYPE:
                onFBBindViewHolder((T) holder,position);

                break;
        }
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return POST_TYPE;
        } else {
            return  (position%adInterval==0)?AD_TYPE:POST_TYPE;
        }
    }

    public  class AdHolder extends RecyclerView.ViewHolder {
        ImageView nativeAdIcon;
        TextView nativeAdTitle,nativeAdSocialContext,nativeAdBody,nativeAdCallToAction;
        MediaView nativeAdMedia;
        List<View> clickableViews = new ArrayList<>();

        LinearLayout nativeAdContainer;
        public AdHolder(View view) {
            super(view);

            nativeAdIcon = (ImageView) view.findViewById(R.id.native_ad_icon);
            nativeAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
            nativeAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
            nativeAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            nativeAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            nativeAdCallToAction = (TextView) view.findViewById(R.id.native_ad_call_to_action);
            clickableViews.add(nativeAdMedia);
            clickableViews.add(nativeAdCallToAction);
        }

        public void bindView(NativeAd ad) {
            // Set the Text.
            if (ad == null) {
                nativeAdSocialContext.setText("No Ad");
                nativeAdTitle.setText("No Ad");
                nativeAdBody.setText("Ad is not loaded.");
            } else {
                nativeAdTitle.setText(ad.getAdTitle());
                nativeAdSocialContext.setText(ad.getAdSocialContext());
                nativeAdBody.setText(ad.getAdBody());
                nativeAdCallToAction.setText(ad.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = ad.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);
                // Download and display the cover image.
                nativeAdMedia.setNativeAd(ad);
                ad.registerViewForInteraction(nativeAdContainer, clickableViews);
            }
        }
    }

    public abstract  RecyclerView.ViewHolder onFBCreateViewHolder(ViewGroup parent);
    public abstract void onFBBindViewHolder(T holder , int position);
}
