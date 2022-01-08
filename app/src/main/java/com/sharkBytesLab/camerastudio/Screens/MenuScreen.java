package com.sharkBytesLab.camerastudio.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MediationUtils;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.sharkBytesLab.camerastudio.BuildConfig;
import com.sharkBytesLab.camerastudio.PrivacyPolicy.PrivacyPolicyActivity;
import com.sharkBytesLab.camerastudio.PrivacyPolicy.TermsActivity;
import com.sharkBytesLab.camerastudio.databinding.ActivityMenuScreenBinding;

public class MenuScreen extends AppCompatActivity {

    private ActivityMenuScreenBinding binding;
    private InterstitialAd mInterstitialAd;
    private String TAG = "Ad Status";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        try {

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


        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(MenuScreen.this, "ca-app-pub-5127713321341585/1470579946", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(MenuScreen.this);

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                Toast.makeText(MenuScreen.this, "Ads Failed to Load.", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();

                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                Toast.makeText(MenuScreen.this, "Ad Clicked.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i(TAG, "onAdLoaded");

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }catch(Exception e)

    {
        Log.d("Result Screen Error : ", e.getMessage());
    }

}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MenuScreen.this, MainScreen.class));
        finish();
    }
}