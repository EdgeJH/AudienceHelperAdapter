package com.edge.audiencehelperadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edge.fbadhelper.FBAdapterSetting;
import com.edge.fbadhelper.FBCustomAdapter;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;

/**
 * Created by user1 on 2017-12-04.
 */

public class AdCustomAdapter extends FBCustomAdapter<AdCustomAdapter.MyHolder,AdCustomAdapter.AdHolder> {
    Context context;
    ArrayList<String> arrayList;
    public AdCustomAdapter(Context context, ArrayList arrayList, FBAdapterSetting setting) {
        super(context, arrayList, setting);
        this.context = context;
        this.arrayList =arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateAllViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case FBCustomAdapter.AD_TYPE :
                View adView = LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false);
                return new AdHolder(adView);
            case FBCustomAdapter.POST_TYPE :
                View myView = LayoutInflater.from(context).inflate(R.layout.example_item,parent,false);
                return new MyHolder(myView);
            default:
                return null;
        }
    }

    @Override
    public void onBindMyViewHolder(MyHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public void onBindFBViewHolder(AdHolder holder, int position, NativeAd nativeAd) {
        holder.bindView(nativeAd);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

    class AdHolder extends RecyclerView.ViewHolder {
        private MediaView mAdMedia;
        private ImageView mAdIcon;
        private TextView mAdTitle;
        private TextView mAdBody;
        private TextView mAdSocialContext;
        private Button mAdCallToAction;

        public AdHolder(View view) {
            super(view);

            mAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
            mAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            mAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            mAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            mAdCallToAction = (Button)view.findViewById(R.id.native_ad_call_to_action);
            mAdIcon = (ImageView)view.findViewById(R.id.native_ad_icon);

        }

        public void bindView(NativeAd ad) {
            if (ad == null) {
                mAdTitle.setText("No Ad");
                mAdBody.setText("Ad is not loaded.");
            }
            else {
                mAdTitle.setText(ad.getAdTitle());
                mAdBody.setText(ad.getAdBody());
                mAdSocialContext.setText(ad.getAdSocialContext());
                mAdCallToAction.setText(ad.getAdCallToAction());
                mAdMedia.setNativeAd(ad);
                NativeAd.Image adIcon = ad.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, mAdIcon);
            }
        }
    }
}
