package com.sharkBytesLab.camerastudio.PrivacyPolicy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sharkBytesLab.camerastudio.Screens.MenuScreen;
import com.sharkBytesLab.camerastudio.databinding.ActivityTermsBinding;

public class TermsActivity extends AppCompatActivity {

    ActivityTermsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        try {

            binding.termsWebView.getSettings().setJavaScriptEnabled(true);
            binding.termsWebView.loadUrl("file:///android_asset/terms.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TermsActivity.this, MenuScreen.class));
        finish();
    }
}