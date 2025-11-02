package com.example.capstone;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView; // Tambahkan ini di atas

public class ResponseDialog {

    private final Dialog dialog;
    private final TextView inputText;
    private final TextView outputText;
    private final TextView inputLabel;
    private final TextView outputLabel;
    private final ProgressBar loading;
    private final Context context;

    public ResponseDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_response, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);

        // Membuat background transparan agar sudut rounded terlihat
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Lebar dialog 90% dari layar, tinggi menyesuaikan isi
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels * 0.9);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        inputText = view.findViewById(R.id.text_input_content);
        outputText = view.findViewById(R.id.text_output_content);
        inputLabel = view.findViewById(R.id.text_input);
        outputLabel = view.findViewById(R.id.text_output);
        loading = view.findViewById(R.id.loading);
        // ✅ Tambahkan ini untuk tombol close
        ImageView btnClose = view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    public void show(String userInput) {
        inputText.setText(userInput);
        outputText.setVisibility(View.GONE);
        outputLabel.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        dialog.show();
    }

    public void updateOutput(String output) {
        loading.setVisibility(View.GONE);
        outputLabel.setVisibility(View.VISIBLE);
        outputText.setVisibility(View.VISIBLE);
        outputText.setText(output);
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
