package com.sharkBytesLab.camerastudio.Screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.sharkBytesLab.camerastudio.R;
import com.sharkBytesLab.camerastudio.databinding.ActivityMainScreenBinding;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class MainScreen extends AppCompatActivity implements MaxRewardedAdListener {


    private ActivityMainScreenBinding binding;
    private int IMAGE_REQUEST_CODE = 45;
    private int CAMERA_REQUEST_CODE = 14;
    private int RESULT_CODE = 200;
    boolean isPressed = false;
    private MaxRewardedAd rewardedAd;
    private int retryAttempt;
    private final String TAG = "Rewarded Ads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportActionBar().hide();

        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {

            }
        } );

        rewardedAd = MaxRewardedAd.getInstance("e061615381f3637c", this);
        rewardedAd.setListener(this);
        rewardedAd.loadAd();
        binding.applovinAd.loadAd();

        binding.compressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), CompressScreen.class));
//                finish();
                Toast.makeText(getApplicationContext(), "This feature is under Maintenance", Toast.LENGTH_SHORT).show();
            }
        });

        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rewardedAd.isReady()) {
                    rewardedAd.showAd();
                    Log.i(TAG, "Show");
                } else {
                    showToast("Getting Ads...");
                }
                startActivity(new Intent(MainScreen.this, MenuScreen.class));
                finish();
            }
        });

        binding.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainScreen.this, new String[] {Manifest.permission.CAMERA},2);
                }else
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {


                    if (requestCode == IMAGE_REQUEST_CODE) {
                        if (data.getData() != null) {
                            try {
                                Uri filePath = data.getData();
                                Intent dsPhotoEditorIntent = new Intent(MainScreen.this, DsPhotoEditorActivity.class);
                                dsPhotoEditorIntent.setData(filePath);
                                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "CameraStudio");
                                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                                startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    if (requestCode == RESULT_CODE) {
                        try {
                            Intent intent = new Intent(MainScreen.this, ResultScreen.class);
                            intent.setData(data.getData());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (requestCode == CAMERA_REQUEST_CODE) {
                        try {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            Uri uri = getImageUri(photo);
                            Intent dsPhotoEditorIntent = new Intent(MainScreen.this, DsPhotoEditorActivity.class);
                            dsPhotoEditorIntent.setData(uri);
                            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "CameraStudio");
                            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }catch(Exception exception){
                exception.printStackTrace();

            }
            }
        };
        Thread thread2 = new Thread(runnable2);
        thread2.start();

    }
    public Uri getImageUri(Bitmap bitmap)
    {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title",null);
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {

        if(isPressed)
        {
            finishAffinity();
            System.exit(0);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Press back again to Exit.", Toast.LENGTH_SHORT).show();
            isPressed = true;
        }
        Runnable runnable = new Runnable() {
         @Override
          public void run() {
        isPressed = false;
    }
};
        new Handler().postDelayed(runnable,2000);

    }

    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'

        // Reset retry attempt
        Log.i(TAG, "Loaded");
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        // Rewarded ad failed to load
        // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        Log.i(TAG, "Failed");
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rewardedAd.loadAd();
            }
        }, delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        Log.i(TAG, "Failed");
        // Rewarded ad failed to display. We recommend loading the next ad
        rewardedAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
        Log.i(TAG, "Displayed");
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
    }

    @Override
    public void onAdHidden(final MaxAd maxAd) {
        Log.i(TAG, "Hidden");
        // rewarded ad is hidden. Pre-load the next ad
        rewardedAd.loadAd();
    }

    @Override
    public void onRewardedVideoStarted(final MaxAd maxAd) {
    }

    @Override
    public void onRewardedVideoCompleted(final MaxAd maxAd) {
        Log.i(TAG, "Completed");

        Intent intent = new Intent(getApplicationContext(), MenuScreen.class);
        startActivity(intent);
    }

    @Override
    public void onUserRewarded(final MaxAd maxAd, final MaxReward maxReward) {
        // Rewarded ad was displayed and user should receive the reward
    }

    private void showToast(String s) {
        MotionToast.Companion.createColorToast(this,
                s,
                "TRY AGAIN AFTER SOME TIME",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                10000,
                ResourcesCompat.getFont(this, R.font.helvetica_regular));

    }

}