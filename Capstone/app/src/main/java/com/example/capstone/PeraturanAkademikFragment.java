package com.example.capstone;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.barteksc.pdfviewer.PDFView;

public class PeraturanAkademikFragment extends Fragment {

    private String pdfPath;

    public PeraturanAkademikFragment(String path) {
        this.pdfPath = path;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peraturan_akademik, container, false);
        PDFView pdfView = view.findViewById(R.id.pdfView);
        pdfView.fromAsset("peraturan.pdf")
                .enableSwipe(true)               // Aktifkan swipe gesture
                .swipeHorizontal(true)           // Ganti jadi horizontal (bukan scroll vertikal)
                .enableDoubletap(true)           // Aktifkan zoom dengan double tap (opsional)
                .defaultPage(0)                  // Mulai dari halaman pertama
                .enableAnnotationRendering(true) // Kalau PDF punya highlight/link
                .password(null)                  // Kalau PDF tidak di-protect
                .scrollHandle(null)              // Scrollbar bisa dimatikan (optional)
                .load();

        return view;
    }
}
