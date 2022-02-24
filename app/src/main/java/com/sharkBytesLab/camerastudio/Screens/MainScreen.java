package com.sharkBytesLab.camerastudio.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.sharkBytesLab.camerastudio.databinding.ActivityMainScreenBinding;

import java.io.ByteArrayOutputStream;

public class MainScreen extends AppCompatActivity {


    private ActivityMainScreenBinding binding;
    private int IMAGE_REQUEST_CODE = 45;
    private int CAMERA_REQUEST_CODE = 14;
    private int RESULT_CODE = 200;
    boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportActionBar().hide();

        // Please make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {

            }
        } );

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
}