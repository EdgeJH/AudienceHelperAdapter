package com.edge.fbadhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.Collections;
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
            if ((i%adInterval)==0&&i!=0){
                arrayList.add(i,null);
            }
        }
    }
    public void addData(Object data){
        if ((arrayList.size())%adInterval==0){
            arrayList.add(null);
        }
        arrayList.add(data);
        notifyItemInserted(arrayList.size());
    }
    public void addData(int index ,Object data){
        if (arrayList.size()!=0){
            if ((index%adInterval)!=0){
                arrayList.add(index,data);
                removeSort();
                notifyItemInserted(index);
            }
            if (index==0){
                arrayList.add(0,data);
                removeSort();
                notifyItemInserted(index);
            }
        } else {
            arrayList.add(index,data);
            notifyItemInserted(index);
        }
    }
    public void clear(){
        arrayList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(ArrayList arrayList){
        this.arrayList = arrayList;
        setArrayList();
    }
    public void removeData(int position){
        if ((position%adInterval)!=0){
            arrayList.remove(position);
            removeSort();
            notifyItemRemoved(position);
        }
        if (position==0){
            arrayList.remove(0);
            removeSort();
            notifyItemRemoved(position);
        }
    }

    private void addSort(int index){
        for (int i =0; i< arrayList.size(); i++){
            if ((i%adInterval)==0&&i!=0&&i!=index&&i+1<arrayList.size()){
                Collections.swap(arrayList,i+1,i);
            }
        }
    }
    private void removeSort(){
        for (int i =0; i< arrayList.size(); i++){
            if ((i%adInterval)==0&&i!=0){
                Collections.swap(arrayList,i-1,i);
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
        AdIconView nativeAdIcon;
        TextView nativeAdTitle,nativeAdSocialContext,nativeAdBody,nativeAdCallToAction;
        MediaView nativeAdMedia;
        List<View> clickableViews = new ArrayList<>();

        LinearLayout nativeAdContainer;
        public AdHolder(View view) {
            super(view);

            nativeAdIcon = view.findViewById(R.id.native_ad_icon);
            nativeAdTitle = view.findViewById(R.id.native_ad_title);
            nativeAdContainer =  view.findViewById(R.id.native_ad_container);
            nativeAdMedia =  view.findViewById(R.id.native_ad_media);
            nativeAdSocialContext =view.findViewById(R.id.native_ad_social_context);
            nativeAdBody =  view.findViewById(R.id.native_ad_body);
            nativeAdCallToAction = view.findViewById(R.id.native_ad_call_to_action);

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
                nativeAdTitle.setText(ad.getAdvertiserName());
                nativeAdSocialContext.setText(ad.getAdSocialContext());
                nativeAdBody.setText(ad.getAdBodyText());
                nativeAdCallToAction.setText(ad.getAdCallToAction());
                ad.registerViewForInteraction(nativeAdContainer, nativeAdMedia,nativeAdIcon,clickableViews);
            }
        }
    }

    public abstract  RecyclerView.ViewHolder onFBCreateViewHolder(ViewGroup parent);
    public abstract void onFBBindViewHolder(T holder , int position);
}
