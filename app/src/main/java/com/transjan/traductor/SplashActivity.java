package com.transjan.traductor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    //    InterstitialAd interstitialAd;
    private Handler mWaitHandler = new Handler();
//    private boolean loadAdFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
//        MobileAds.initialize(this,getString(R.string.ad_app_id));
        intent = new Intent(this, StartActivity.class);
//        loadAd();

        ImageView imageView = (ImageView) findViewById(R.id.loading_imgView);
        Glide.with(this).asGif().load(R.drawable.loading).into(imageView);

        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mWaitHandler.removeCallbacksAndMessages(null);
//                if (!loadAdFlag) {
//                    showAd(interstitialAd);
//                    loadAdFlag = true;
//                }
                startMain();
            }
        }, 2000);
    }

    private void loadAd() {
//            interstitialAd =  new InterstitialAd(this);
//            interstitialAd.setAdUnitId(getString(R.string.ad_id_interstitial));
//            interstitialAd.loadAd(new AdRequest.Builder().build());
//            interstitialAd.setAdListener(new AdListener(){
//                public void onAdLoaded() {
//                    if (loadAdFlag) showAd(interstitialAd);
//                }
//
//                public void onAdFailedToLoad(int i) {
//                    if (loadAdFlag) startMain();
//                }
//
//                public void onAdClosed() {
//                    startMain();
//                }
//            });
    }

//        private void showAd(InterstitialAd interstitialAd) {
//            if (interstitialAd.isLoaded()) interstitialAd.show();
//            else if (!interstitialAd.isLoading())startMain();
//        }

    private void startMain() {
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
//        IronSource.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        IronSource.onPause(this);
    }
}
