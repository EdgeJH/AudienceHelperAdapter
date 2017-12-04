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

public class MainCustomActivity extends AppCompatActivity implements FBLoadListener{
    FBAdManager manager;
    RecyclerView recyclerView;
    AdCustomAdapter adapter;
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
        manager = new FBAdManager.Builder("400285197058008_400386333714561",getApplicationContext())
                .setAdLoadCount(20)
                .setListener(this)
                .build();
    }

    private void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void setAdapter(NativeAdsManager nativeAdsManager){
        FBAdapterSetting setting = new FBAdapterSetting.Builder()
                .setAdInterval(10)
                .setAdsManager(nativeAdsManager)
                .build();
        adapter = new AdCustomAdapter(getApplicationContext(),arrayList,setting);
        setRecyclerView();
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
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.insta:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
