package com.sharkBytesLab.camerastudio.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.sharkBytesLab.camerastudio.BuildConfig;
import com.sharkBytesLab.camerastudio.PrivacyPolicy.PrivacyPolicyActivity;
import com.sharkBytesLab.camerastudio.PrivacyPolicy.TermsActivity;
import com.sharkBytesLab.camerastudio.databinding.ActivityMenuScreenBinding;

import java.util.concurrent.TimeUnit;

public class MenuScreen extends AppCompatActivity implements MaxAdListener {

    private ActivityMenuScreenBinding binding;
    private MaxInterstitialAd interstitialAd;

    private MaxAdView adView;
    private int retryAttempt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.applovinAdMenu.loadAd();
        interstitialAd = new MaxInterstitialAd( "6d6f6f3ce801ecbd", this );
        interstitialAd.setListener( this );
        interstitialAd.loadAd();

        binding.supportUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( interstitialAd.isReady() )
                {
                    interstitialAd.showAd();
                }

            }
        });

        binding.watchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuScreen.this, RewardActivity.class));
            }
        });

        binding.homeButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuScreen.this, MainScreen.class));
                finish();
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareMsg = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Camera Studio : Free-Image-Editor");
                    intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
                    startActivity(Intent.createChooser(intent, "Share Via"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error Occured :" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(i);
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(i);
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuScreen.this, PrivacyPolicyActivity.class));
                finish();
            }
        });

        binding.terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuScreen.this, TermsActivity.class));
                finish();
            }
        });

        binding.version.setText("Version : " + String.valueOf(BuildConfig.VERSION_NAME));

        binding.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuScreen.this, FeedbackScreen.class));
                finish();
            }
        });




            }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MenuScreen.this, MainScreen.class));
        finish();
    }

    @Override
    public void onAdLoaded(final MaxAd maxAd)
    {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'
        // Reset retry attempt
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error)
    {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                interstitialAd.loadAd();
            }
        }, delayMillis );
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
    {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdHidden(final MaxAd maxAd)
    {
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd();
    }


}