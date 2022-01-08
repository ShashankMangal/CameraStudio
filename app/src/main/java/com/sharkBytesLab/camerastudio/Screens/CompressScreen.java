package com.sharkBytesLab.camerastudio.Screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sharkBytesLab.camerastudio.R;
import com.sharkBytesLab.camerastudio.databinding.ActivityCompressScreenBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class CompressScreen extends AppCompatActivity {

    private ActivityCompressScreenBinding binding;
    private  Thread thread1;
    private File originalImage, compressedImage;
    private static String filePath;
    Bitmap selectedImage, compressedImg;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Camera Studio Compress");

    public static final int RESULT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompressScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();



        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {



        askPermission();

        //code
        filePath = path.getAbsolutePath();
        if(!path.exists())
            path.mkdirs();

        binding.backCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainScreen.class));
                finish();
            }
        });

        binding.seekQuality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.textQuality.setText("Quality : "+i+"%");
                seekBar.setMax(100);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.compressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quality = binding.seekQuality.getProgress();
                if(quality == 0)
                    quality = 30;

                if(binding.txtHeight.getText().toString().isEmpty())
                {
                    binding.txtHeight.setError("Enter Height");
                    binding.txtHeight.requestFocus();

                }
                else if(binding.txtWidth.getText().toString().isEmpty())
                {
                    binding.txtWidth.setError("Enter Width");
                    binding.txtWidth.requestFocus();

                }else{



                try {

                    int width = Integer.parseInt(binding.txtWidth.getText().toString());
                    int height = Integer.parseInt(binding.txtHeight.getText().toString());



                    compressedImg = ShrinkBitmap(originalImage.toString(), width, height);
                    Uri location = saveImage(compressedImg);
                    compressedImage = new File(location.getPath());
                    Toast.makeText(getApplicationContext(), "Compressed Image Saved.", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Log.d("Try Error : ",e.getMessage());
                    e.printStackTrace();
                }


        }
            }
        });
            }
        };
        thread1 = new Thread(runnable1);
        thread1.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainScreen.class));
        finish();
    }

    private void askPermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, RESULT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            binding.compressButton.setVisibility(View.VISIBLE);

            final Uri imageUri = data.getData();
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                binding.imgOriginal.setImageBitmap(selectedImage);
                originalImage = new File(imageUri.getPath().replace("raw/",""));
                binding.txtOriginal.setText("Size : "+ Formatter.formatShortFileSize(this, originalImage.length()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went Wrong!", Toast.LENGTH_SHORT).show();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "No Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap ShrinkBitmap(String file, int width, int height)
    {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bit = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if(heightRatio > 1 || widthRatio > 1)
        {
            if(heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            }
            else
            {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bit = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bit;
    }

    private Uri saveImage(Bitmap bitmap)
    {
        OutputStream fos;
        Uri imageUri = null;
        try
        {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Camera Studio Compress"+".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);


        }
        }catch(Exception e)
        {
            Log.d("Save Image Error : ",e.getMessage());
        }
        return imageUri;
    }

    @Override
    protected void onStop() {
        super.onStop();

        thread1.interrupt();

    }
}