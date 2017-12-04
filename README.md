AudienceHelperAdapter
===============
FaceBook Audience Helper

![Audience](/Audience.gif)


Gradle
------------
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```groovy
dependencies {
    compile 'com.facebook.android:audience-network-sdk:4.+'
    compile 'com.github.EdgeJH:AudienceHelperAdapter:1.0.0'
}
```

Usage
--------
### Activity 

```java
public class MainActivity extends AppCompatActivity implements FBLoadListener{


    private void setManager(){
        manager = new FBAdManager.Builder("YOUR PLACEMENT ID",getApplicationContext())
                .setAdLoadCount(20)
                .setListener(this)
                .isCaching(true)
                .build();
    }

    private void setAdapter(NativeAdsManager nativeAdsManager){
        FBAdapterSetting setting = new FBAdapterSetting.Builder()
                .setAdInterval(10)
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
 }
```

### Instagram Layout Audience Adapter

```java
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
    public void onFBBindViewHolder(CustomHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }


    class CustomHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public CustomHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }

}
```


### MyCustomLayout Audience Adapter

```java
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

```

License
--------
```
Copyright 2017 EdgeJH


Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```
