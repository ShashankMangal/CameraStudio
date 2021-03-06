package com.sharkBytesLab.camerastudio.PrivacyPolicy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharkBytesLab.camerastudio.R;
import com.sharkBytesLab.camerastudio.Screens.MainScreen;
import com.sharkBytesLab.camerastudio.Screens.MenuScreen;
import com.sharkBytesLab.camerastudio.databinding.ActivityPrivacyPolicyBinding;

import org.sufficientlysecure.htmltextview.HtmlFormatter;
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder;

import java.net.Inet4Address;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ActivityPrivacyPolicyBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        try {

            binding.policyWebView.getSettings().setJavaScriptEnabled(true);
            binding.policyWebView.loadUrl("file:///android_asset/policy.html");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PrivacyPolicyActivity.this, MenuScreen.class));
        finish();
    }
}