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

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.sharkBytesLab.camerastudio.databinding.ActivityMainScreenBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        getSupportActionBar().hide();

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

        if(requestCode == IMAGE_REQUEST_CODE)
        {
            if(data.getData() != null) {
                try {
                    Uri filePath = data.getData();
                    Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                    dsPhotoEditorIntent.setData(filePath);
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "CameraStudio");
                    int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                    dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                    startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }


        if(requestCode == RESULT_CODE)
        {
            try{
            Intent intent = new Intent(MainScreen.this , ResultScreen.class);
            intent.setData(data.getData());
            startActivity(intent);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        if(requestCode == CAMERA_REQUEST_CODE)
        {
            try{
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageUri(photo);
            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "CameraStudio");
            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
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