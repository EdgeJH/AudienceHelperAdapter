package com.edge.fbadhelper;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;

/**
 * Created by user1 on 2017-12-01.
 */

public interface FBLoadListener {
    void onLoadSuccess(NativeAdsManager adsManager);

    void onLoadFail(AdError error);
}
