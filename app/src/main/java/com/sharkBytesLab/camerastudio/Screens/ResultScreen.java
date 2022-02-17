package com.sharkBytesLab.camerastudio.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.sharkBytesLab.camerastudio.databinding.ActivityResultScreenBinding;

import java.io.IOException;

public class ResultScreen extends AppCompatActivity {

    private ActivityResultScreenBinding binding;
    private int BACK_REQUEST_CODE = 100;
    private WallpaperManager wallpaperManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.progressBar.setVisibility(View.VISIBLE);


        try {
            Log.i("Image Source :", getIntent().getData().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }





                    wallpaperManager = WallpaperManager.getInstance(getApplicationContext());


                    if (getIntent().getData() != null) {
                        binding.finalImage.setImageURI(getIntent().getData());
                    }
                    if (getIntent().getData() == null) {
                        finishAffinity();
                        System.exit(0);
                    }
                    binding.homeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ResultScreen.this, MainScreen.class));
                            Toast.makeText(getApplicationContext(), "Image Saved..", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    binding.shareButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(getApplicationContext(), "Share Result", Toast.LENGTH_SHORT).show();

                            Uri uri = getIntent().getData();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(intent, "Share"));

                        }
                    });

                    binding.wallpaperButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "Wallpaper Applied", Toast.LENGTH_SHORT).show();

                            try {
                                Bitmap bitmap = ((BitmapDrawable) binding.finalImage.getDrawable()).getBitmap();
                                wallpaperManager.setBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    binding.closeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });


                    binding.whatsappButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(getApplicationContext(), "Share On WhatsApp", Toast.LENGTH_SHORT).show();

                            Uri uri = getIntent().getData();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setPackage("com.whatsapp");
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    binding.backButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Uri filePath = getIntent().getData();
                            Intent dsPhotoEditorIntent = new Intent(ResultScreen.this, DsPhotoEditorActivity.class);
                            dsPhotoEditorIntent.setData(filePath);
                            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "CameraStudio");
                            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                            startActivityForResult(dsPhotoEditorIntent, BACK_REQUEST_CODE);
                        }
                    });



        }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == BACK_REQUEST_CODE) {
            binding.finalImage.setImageURI(data.getData());

        }

    }
    private String getExifTag(ExifInterface exif,String tag){
        String attribute = exif.getAttribute(tag);

        return (null != attribute ? attribute : "");
    }


}