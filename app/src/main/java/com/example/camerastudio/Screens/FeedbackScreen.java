package com.example.camerastudio.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.camerastudio.R;
import com.example.camerastudio.databinding.ActivityFeedbackScreenBinding;

public class FeedbackScreen extends AppCompatActivity {

    private ActivityFeedbackScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();


        String email = "shashankmangal10@gmail.com";
        String subject = "Camera Studio feedback";

        binding.sendButtonReport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                i.putExtra(Intent.EXTRA_SUBJECT,subject);
                i.setData(Uri.parse("mailto:"));
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Type your feedback here.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FeedbackScreen.this, MenuScreen.class));
        finish();
    }
}