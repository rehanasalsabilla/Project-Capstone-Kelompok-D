package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.widget.TextView;


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Kalau mau lanjut ke MainActivity pas klik Start
        Button startButton = findViewById(R.id.buttonStart);
        startButton.setOnClickListener(v -> {
            // Intent untuk pindah ke MainActivity (ganti sesuai nama activity-mu)
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: menutup WelcomeActivity supaya tidak bisa balik
        });
        TextView instagramIT = findViewById(R.id.instagramIT);
        instagramIT.setOnClickListener(v -> {
            String url = "https://www.instagram.com/its_teknologi_informasi/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        TextView instagramHMIT = findViewById(R.id.instagramHMIT);
        instagramHMIT.setOnClickListener(v -> {
            String url = "https://www.instagram.com/hmit_its/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        TextView linkedinIT = findViewById(R.id.linkedinIT);
        linkedinIT.setOnClickListener(v -> {
            String url = "https://www.instagram.com/hmit_its/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        TextView YoutubeIT = findViewById(R.id.youtubeIT);
        YoutubeIT.setOnClickListener(v -> {
            String url = "https://www.youtube.com/@informationtechnologyits3231";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        TextView WebsiteIT = findViewById(R.id.WebsiteIT);
        WebsiteIT.setOnClickListener(v -> {
            String url = "https://www.its.ac.id/it/id/departemen-teknologi-informasi/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

    }
}
