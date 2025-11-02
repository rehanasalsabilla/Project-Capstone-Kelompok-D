package com.example.capstone;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends BaseActivity {

    ImageButton btnProgramStudi, btnPeraturanAkademik, btnKalenderAkademik, btnPendaftaran, btnAkreditasi, btnProspekKarir, btnPerkuliahan, btnOrganisasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());

        btnProgramStudi = findViewById(R.id.btnProgramStudi);
        btnPeraturanAkademik = findViewById(R.id.btnPeraturanAkademik);
        btnKalenderAkademik = findViewById(R.id.btnKalenderAkademik);
        btnPendaftaran = findViewById(R.id.btnPendaftaran);
        btnAkreditasi = findViewById(R.id.btnAkreditasi);
        btnProspekKarir = findViewById(R.id.btnProspekKarir);
        btnPerkuliahan = findViewById(R.id.btnPerkuliahan);
        btnOrganisasi = findViewById(R.id.btnOrganisasi);



        btnProgramStudi.setOnClickListener(v -> {
            replaceFragment(new ProgramStudiFragment());
        });

        btnPeraturanAkademik.setOnClickListener(v -> {
            replaceFragment(new PeraturanAkademikFragment("file:///android_asset/peraturan.pdf"));
        });

        btnKalenderAkademik.setOnClickListener(v -> {
            replaceFragment(new KalenderAkademikFragment("file:///android_asset/kalenderakademik.pdf"));
        });

        btnPendaftaran.setOnClickListener(v -> {
            replaceFragment(new PendaftaranFragment());
        });

        btnAkreditasi.setOnClickListener(v -> {
            replaceFragment(new AkreditasiFragment());
        });

        btnProspekKarir.setOnClickListener(v -> {
            replaceFragment(new ProspekKarirFragment());
        });

        btnPerkuliahan.setOnClickListener(v -> {
            replaceFragment(new PerkuliahanFragment());
        });
        btnOrganisasi.setOnClickListener(v -> {
            replaceFragment(new OrganisasiFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        final androidx.core.widget.NestedScrollView scrollView = findViewById(R.id.scrollView);
        final View fragmentView = findViewById(R.id.fragmentContainer);

        if (scrollView != null && fragmentView != null) {
            scrollView.post(() -> scrollView.smoothScrollTo(0, fragmentView.getTop()));
        }
    }

}
