package com.edge.audiencehelperadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edge.fbadhelper.FBAdapter;
import com.edge.fbadhelper.FBAdapterSetting;

import java.util.ArrayList;

/**
 * Created by user1 on 2017-12-04.
 */

public class AdAdapter extends FBAdapter<AdAdapter.CustomHolder> {
    Context context;
    ArrayList<String> arrayList=new ArrayList<>();

    public AdAdapter(Context context, ArrayList arrayList, FBAdapterSetting setting) {
        super(context, arrayList, setting);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onFBCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.example_item,parent,false);
        return  new CustomHolder(view);
    }

    @Override
    public void onFBBindViewHolder(final CustomHolder holder, int position) {
        holder.textView.setText(String.valueOf(arrayList.get(position)));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(holder.getAdapterPosition());
            }
        });
    }


    class CustomHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public CustomHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

}
