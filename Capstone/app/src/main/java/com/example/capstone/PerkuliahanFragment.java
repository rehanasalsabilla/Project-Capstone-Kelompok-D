package com.example.capstone;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PerkuliahanFragment extends Fragment {

    public PerkuliahanFragment() {
        // Konstruktor kosong dibutuhkan oleh Fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perkuliahan, container, false);

        ImageButton openFileButton = view.findViewById(R.id.openFileButton); // Pastikan ID ini sama dengan XML

        openFileButton.setOnClickListener(v -> {
            try {
                String fileName = "perkuliahandti.pdf";

                // Salin file dari assets ke cache
                File outFile = new File(requireContext().getCacheDir(), fileName);
                if (!outFile.exists()) {
                    AssetManager assetManager = requireContext().getAssets();
                    InputStream in = assetManager.open(fileName);
                    FileOutputStream out = new FileOutputStream(outFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                }

                // Buat URI menggunakan FileProvider
                Uri uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().getPackageName() + ".provider",
                        outFile
                );

                // Intent untuk membuka PDF
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Buka PDF dengan"));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Gagal membuka file", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
