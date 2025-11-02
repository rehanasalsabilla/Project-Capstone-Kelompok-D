package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class ProfilActivity extends AppCompatActivity {

    private LinearLayout menuLayout;
    private PlayerView playerView;
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        playerView = findViewById(R.id.playerView);
        menuLayout = findViewById(R.id.menuLayout);
        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());

        // Inisialisasi ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Load video dari res/raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play(); // Auto play

        // Set default fragment
        showFragment(new SejarahFragment());

        // Terapkan margin awal seperti tombol Sejarah diklik
        View btnSejarah = findViewById(R.id.btnSejarah);
        adjustButtonMargin(btnSejarah);

        // Set listener tombol menu
        btnSejarah.setOnClickListener(v -> {
            showFragment(new SejarahFragment());
            adjustButtonMargin(v);
        });
        findViewById(R.id.btnVisiMisi).setOnClickListener(v -> {
            showFragment(new VisiMisiFragment());
            adjustButtonMargin(v);
        });
        findViewById(R.id.btnDosen).setOnClickListener(v -> {
            showFragment(new DosenFragment());
            adjustButtonMargin(v);
        });
        findViewById(R.id.btnFasilitas).setOnClickListener(v -> {
            showFragment(new FasilitasFragment());
            adjustButtonMargin(v);
        });
    }

    // Fungsi untuk menampilkan fragment
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    // Fungsi margin tombol
    private void adjustButtonMargin(View clickedButton) {
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            View button = menuLayout.getChildAt(i);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
            if (button != clickedButton) {
                params.topMargin = 20;
            } else {
                params.topMargin = 0;
            }
            button.setLayoutParams(params);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
