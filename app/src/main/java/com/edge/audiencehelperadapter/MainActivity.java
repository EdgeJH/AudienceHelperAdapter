package com.edge.audiencehelperadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.edge.fbadhelper.FBAdManager;
import com.edge.fbadhelper.FBAdapterSetting;
import com.edge.fbadhelper.FBLoadListener;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FBLoadListener{
    FBAdManager manager;
    RecyclerView recyclerView;
    AdAdapter adapter;
    ArrayList<String> arrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setArrayList();
        initView();
        setManager();
    }

    private void initView(){
        recyclerView= findViewById(R.id.recycler);
    }

    private void setArrayList() {
        for (int i = 0; i < 30; i++) {
            arrayList.add(String.valueOf(i) + String.valueOf(i) + String.valueOf(i));
        }
    }

    private void setManager(){
        manager = new FBAdManager.Builder("YOUR_PLACEMENT_ID",getApplicationContext())
                .setAdLoadCount(20)
                .setListener(this)
                .isCaching(true)
                .build();
    }

    private void setAdapter(NativeAdsManager nativeAdsManager){
        FBAdapterSetting setting = new FBAdapterSetting.Builder()
                .setAdInterval(3)
                .setAdsManager(nativeAdsManager)
                .build();
        adapter = new AdAdapter(getApplicationContext(),arrayList,setting);
        setRecyclerView();
    }

    private void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onLoadSuccess(NativeAdsManager adsManager) {
        setAdapter(adsManager);
    }



    @Override
    public void onLoadFail(AdError error) {
        setAdapter(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.custom:
                Intent intent = new Intent(this,MainCustomActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.add:
                int i = arrayList.size();
                adapter.addData(String.valueOf(i) + String.valueOf(i) + String.valueOf(i));
                return true;
            case R.id.add_index:
                int i2 = arrayList.size();
                adapter.addData(3,String.valueOf(i2) + String.valueOf(i2) + String.valueOf(i2));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
