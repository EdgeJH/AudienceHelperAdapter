package com.edge.fbadhelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;

/**
 * Created by user1 on 2017-12-01.
 */

public abstract class FBCustomAdapter<T extends RecyclerView.ViewHolder,E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    ArrayList arrayList;
    Context context;
    public static final int POST_TYPE = 1;
    public static final int AD_TYPE = 2;
    private  int adInterval=10;

    private NativeAdsManager mAds;
    private NativeAd mAd;
    private FBAdapterSetting setting;
    public FBCustomAdapter(Context  context,ArrayList arrayList,FBAdapterSetting setting ) {
        this.context =context;
        this.arrayList = arrayList;
        this.setting =setting;
        this.adInterval = this.setting.getAdInterval();
        this.mAds = this.setting.getmAds();
        setArrayList();
    }

    private void setArrayList(){

        for (int i =0; i<arrayList.size(); i++){
            if ((i%adInterval)==0&&i!=0){
                arrayList.add(i,null);
            }
        }
    }

    public void addData(Object data) {
        if ((arrayList.size()) % adInterval == 0) {
            arrayList.add(null);
        }
        arrayList.add(data);
        notifyItemInserted(arrayList.size());
    }

    public void addData(int index, Object data) {
        if (arrayList.size() != 0) {
            if (index % adInterval == 0 && index != 0) {
                if (index == arrayList.size()) {
                    addData(data);
                } else {
                    arrayList.add(index,data);
                    sortArr();
                }
            } else {
                arrayList.add(index, data);
                sortArr();
            }
            notifyItemInserted(index);
        } else {
            arrayList.add(index, data);
            notifyItemInserted(index);
        }
    }

    public void clear() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(ArrayList arrayList) {
        this.arrayList = arrayList;
        setArrayList();
    }


    public void removeData(int position) {
        if ((position % adInterval) != 0) {
            arrayList.remove(position);
            sortArr();
            notifyItemRemoved(position);
        }
    }

    private void sortArr() {
        for (int i = 0; i < arrayList.size(); i++) {
            if ((i % adInterval) == 0 && i != 0 ) {
                if (arrayList.get(i)!=null){
                    arrayList.add(i,null);
                }
            } else {
                if (arrayList.get(i)==null){
                    arrayList.remove(i);
                }
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateAllViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case AD_TYPE:
                if (mAds != null && mAds.isLoaded()) {
                    mAd = mAds.nextNativeAd();
                    onBindFBViewHolder((E) holder,position,mAd);
                }
                else {
                    onBindFBViewHolder((E) holder,position,null);
                }
                break;
            case POST_TYPE:
                onBindMyViewHolder((T) holder,position);
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
    public abstract RecyclerView.ViewHolder onCreateAllViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindMyViewHolder(T holder, int position);
    public abstract  void onBindFBViewHolder(E holder, int position, NativeAd nativeAd);


}
