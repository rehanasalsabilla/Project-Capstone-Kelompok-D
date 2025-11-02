package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 detik (durasi splash)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView robotImage = findViewById(R.id.robotImage);

        // Load animasi zoom in
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        robotImage.startAnimation(zoomIn);

        // Setelah durasi splash, langsung ke WelcomeActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish(); // supaya splash screen tidak bisa kembali
        }, SPLASH_DURATION);
    }
}
