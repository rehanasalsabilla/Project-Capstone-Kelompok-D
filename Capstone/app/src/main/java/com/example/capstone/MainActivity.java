package com.example.capstone;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.animation.ObjectAnimator;

import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends BaseActivity {

    ImageButton btnProfil, btnPrestasi, btnGaleri, btnInfo, btnKarakter;
    ImageView imageViewKarakter;
    int[] karakterImages = {R.drawable.char2, R.drawable.char3, R.drawable.char4};
    int selectedKarakter = 0;
    Handler handler = new Handler();
    SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "karakter_pref";
    private static final String KEY_SELECTED_KARAKTER = "selected_karakter";
   private static final int REQUEST_AUDIO_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showWelcomePopup();
        // Hubungkan komponen
        btnProfil = findViewById(R.id.btnProfil);
        btnPrestasi = findViewById(R.id.btnPrestasi);
        btnGaleri = findViewById(R.id.btnGaleri);
        btnInfo = findViewById(R.id.btnInfo);
        btnKarakter = findViewById(R.id.btnKarakter);
        imageViewKarakter = findViewById(R.id.imageViewKarakter);
        imageViewKarakter.setVisibility(View.INVISIBLE);
        ImageButton btnSettings = findViewById(R.id.settings);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        selectedKarakter = sharedPreferences.getInt(KEY_SELECTED_KARAKTER, -1);
// Mengecek izin untuk merekam audio
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION);
            } else {
                onPermissionGranted();
            }
        } else {
            onPermissionGranted();
        }
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // Tombol Pilih Karakter
        btnKarakter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKarakterDialog();
            }
        });

        // Tombol Menu untuk pindah ke halaman lainnya
        setupMenuButton(btnProfil, ProfilActivity.class);
        setupMenuButton(btnPrestasi, PrestasiActivity.class);
        setupMenuButton(btnGaleri, GaleriActivity.class);
        setupMenuButton(btnInfo, InfoActivity.class);



    }
    // Menangani proses ketika izin diberikan
    private void onPermissionGranted() {
        initializeSpeechComponents(); // Memanggil metode ini yang sudah ada di BaseActivity
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            } else {
                Toast.makeText(this, "Izin mikrofon dibutuhkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void handleSpeechResult(String speech) {
        super.handleSpeechResult(speech);
    }

    @Override
    protected void handleLLMResponse(String response) {
        super.handleLLMResponse(response);
    }

    @Override
    protected void onWakeWordDetected() {
        super.onWakeWordDetected();
    }

    @Override
    protected void onSpeechComplete() {
        super.onSpeechComplete();
    }

    // Fungsi untuk setup listener untuk tombol menu
    private void setupMenuButton(ImageButton button, final Class<?> targetActivity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKarakter != -1) {
                    showCharacterNearButton(button);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this, targetActivity));
                        }
                    }, 3000);
                } else {
                    startActivity(new Intent(MainActivity.this, targetActivity));
                }
            }
        });
    }
    private void showKarakterDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);  // Gunakan style custom
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_image_picker, null);
        dialogView.setBackgroundResource(R.drawable.rounded_dialog); // Menetapkan background rounded

        final ImageView imageView = dialogView.findViewById(R.id.imageView);
        ImageButton btnPrevious = dialogView.findViewById(R.id.btnPrevious);
        ImageButton btnNext = dialogView.findViewById(R.id.btnNext);

        if (selectedKarakter == -1) {
            selectedKarakter = 0;
        }
        imageView.setImageResource(karakterImages[selectedKarakter]);

        // Tombol Previous untuk melihat gambar sebelumnya (looping ke gambar terakhir jika sudah di gambar pertama)
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKarakter > 0) {
                    selectedKarakter--;
                } else {
                    selectedKarakter = karakterImages.length - 1; // Kembali ke gambar terakhir jika sudah di gambar pertama
                }
                imageView.setImageResource(karakterImages[selectedKarakter]);
                Log.d("Dialog", "Character selected: " + selectedKarakter);
            }
        });

        // Tombol Next untuk melihat gambar berikutnya (looping ke gambar pertama jika sudah di gambar terakhir)
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedKarakter < karakterImages.length - 1) {
                    selectedKarakter++;
                } else {
                    selectedKarakter = 0; // Kembali ke gambar pertama jika sudah di gambar terakhir
                }
                imageView.setImageResource(karakterImages[selectedKarakter]);
                Log.d("Dialog", "Character selected: " + selectedKarakter);
            }
        });

        // Ganti pemilihan karakter dengan klik pada gambar karakter
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update gambar karakter pada ImageView utama
                imageViewKarakter.setImageResource(karakterImages[selectedKarakter]);
                imageViewKarakter.setVisibility(View.INVISIBLE);

                // Perbarui gambar pada btnKarakter
                btnKarakter.setImageResource(karakterImages[selectedKarakter]);

                // Simpan pilihan karakter di SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_SELECTED_KARAKTER, selectedKarakter);
                editor.apply();

                // Tutup dialog setelah karakter diperbarui
                AlertDialog dialog = builder.create(); // Membuat objek dialog
                dialog.dismiss(); // Menutup dialog
            }
        });

        builder.setView(dialogView);
        builder.setCancelable(true); // Dialog dapat ditutup
        final AlertDialog dialog = builder.create(); // Membuat objek dialog

        dialog.show(); // Menampilkan dialog

        // Tutup dialog ketika memilih karakter
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewKarakter.setImageResource(karakterImages[selectedKarakter]);
                imageViewKarakter.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(KEY_SELECTED_KARAKTER, selectedKarakter);
                editor.apply();

                // Dismiss dialog setelah memilih karakter
                dialog.dismiss();
            }
        });
    }

    private void showCharacterNearButton(ImageButton button) {
        // Mengambil root layout
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        // Menghubungkan imageViewKarakter ke tombol di sebelah kanan
        set.connect(R.id.imageViewKarakter, ConstraintSet.TOP, button.getId(), ConstraintSet.TOP);
        set.connect(R.id.imageViewKarakter, ConstraintSet.START, button.getId(), ConstraintSet.END);
        set.setMargin(R.id.imageViewKarakter, ConstraintSet.TOP, 50); // misalnya 24dp ke bawah
        set.setMargin(R.id.imageViewKarakter, ConstraintSet.START, -10);
        // Menyesuaikan ukuran imageViewKarakter
        set.constrainWidth(R.id.imageViewKarakter, 150);
        set.constrainHeight(R.id.imageViewKarakter, 150);

        // Terapkan perubahan layout
        set.applyTo(layout);

        // Mengubah gambar karakter yang dipilih
        imageViewKarakter.setImageResource(karakterImages[selectedKarakter]);

        // Animasi geleng-geleng
        imageViewKarakter.setVisibility(View.VISIBLE);
        imageViewKarakter.setPivotX(imageViewKarakter.getWidth() / 2f);
        imageViewKarakter.setPivotY(imageViewKarakter.getHeight() / 2f); // titik tumpu di bawah

        ObjectAnimator shake = ObjectAnimator.ofFloat(
                imageViewKarakter,
                "rotation",
                0f, 8f, -8f, 6f, -6f, 3f, -3f, 0f
        );
        shake.setDuration(2000); // durasi total: 1.2 detik
        shake.setInterpolator(new android.view.animation.DecelerateInterpolator());
        shake.start();


    }
    private void showWelcomePopup() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_welcome, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .create();

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // agar corner terlihat
        dialog.show();

        // Tutup jika diklik bagian dalam
        popupView.setOnClickListener(v -> dialog.dismiss());

        // Cari tombol "Siap!" dan pasang listener untuk tutup dialog
        Button btnReady = popupView.findViewById(R.id.btnClose); // atau id tombol yang kamu pakai, misal btnReady
        btnReady.setOnClickListener(v -> dialog.dismiss());
    }



}
