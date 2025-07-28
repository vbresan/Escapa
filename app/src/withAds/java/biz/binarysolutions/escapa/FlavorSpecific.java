package biz.binarysolutions.escapa;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FlavorSpecific {

    private final Activity activity;

    public FlavorSpecific(Activity activity) {
        this.activity = activity;
    }

    public void initialize() {

        MobileAds.initialize(activity);
        AdView mAdView = (AdView) activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
