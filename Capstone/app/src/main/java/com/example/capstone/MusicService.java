package com.example.capstone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "MusicServiceChannel";
    private DatabaseReference databaseReference;
    private MediaPlayer mediaPlayer;
    private boolean alreadyPlaying = false;
    private boolean playRequestedWhilePlaying = false;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this); // pastikan Firebase terinisialisasi
        createNotificationChannel();

        // Koneksi ke Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("sensor/ultrasonik/status");

        // Listener Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);

                    if ("ADA".equalsIgnoreCase(status)) {
                        if (!alreadyPlaying) {
                            playMusicOnce();
                        } else {
                            playRequestedWhilePlaying = true; // permintaan putar ulang saat sedang main
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error reading Firebase, no action needed
            }
        });

        // Notification untuk menjaga service tetap hidup
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Service Running")
                .setContentText("Monitoring sensor status...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        startForeground(1, notification);
    }

    private void playMusicOnce() {
        alreadyPlaying = true;
        playRequestedWhilePlaying = false;

        // Hentikan instance lama kalau ada
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Buat MediaPlayer baru
        mediaPlayer = MediaPlayer.create(this, R.raw.vira);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
                alreadyPlaying = false;

                // Jika selama playback ada permintaan baru, putar ulang
                if (playRequestedWhilePlaying) {
                    playMusicOnce();
                }
            });
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
