package com.example.capstone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceRecognitionDialog extends DialogFragment {

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private TextView voicePrompt, inputText, answerText;
    private ProgressBar progressBar;

    private Runnable onDismissCallback;

    private Handler handler = new Handler();
    private Runnable autoDismissRunnable = this::dismiss;

    public VoiceRecognitionDialog(Runnable onDismissCallback) {
        this.onDismissCallback = onDismissCallback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_voice_recognition);

        voicePrompt = dialog.findViewById(R.id.voicePrompt);
        progressBar = dialog.findViewById(R.id.progressBar);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onResults(Bundle results) {
                progressBar.setVisibility(View.GONE);
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);

                    processCommand(recognizedText);

                    // Dengarkan lagi setelah 2 detik
                    handler.postDelayed(VoiceRecognitionDialog.this::startListening, 2000);
                }
            }

            @Override public void onError(int error) {
                voicePrompt.setText("Gagal mengenali suara.");
                progressBar.setVisibility(View.GONE);
                handler.postDelayed(VoiceRecognitionDialog.this::startListening, 2000);
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onEndOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        startListening();

        return dialog;
    }

    private void startListening() {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        speechRecognizer.startListening(intent);

        resetAutoDismissTimer();
    }

    private void resetAutoDismissTimer() {
        handler.removeCallbacks(autoDismissRunnable);
        handler.postDelayed(autoDismissRunnable, 10000); // tutup jika tidak ada input dalam 5 detik
    }

    private void processCommand(String command) {
        command = command.toLowerCase();
        String response;

        if (command.contains("siapa kamu")) {
            response = "Saya adalah asisten suara Departemen Teknologi Informasi Institut Teknologi Sepuluh November.";
        } else if (command.contains("waktu sekarang")) {
            response = "Waktu sekarang adalah " + DateFormat.getDateTimeInstance().format(new java.util.Date());
        } else if (command.contains("kembali")) {
            response = "Kembali ke halaman sebelumnya.";
            requireActivity().runOnUiThread(() ->
                    new Handler().postDelayed(requireActivity()::finish, 2000)
            );
        } else if (command.contains("beranda") || command.contains("home")) {
            response = "Membuka halaman utama Departemen Teknologi Informasi ITS...";
            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            });
        } else if (command.contains("profil")) {
            response = "Membuka halaman profil.";
            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(requireActivity(), ProfilActivity.class);
                startActivity(intent);
            });
        } else if (command.contains("prestasi")) {
            response = "Membuka halaman prestasi.";
            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(requireActivity(), PrestasiActivity.class);
                startActivity(intent);
            });
        } else if (command.contains("galeri")) {
            response = "Membuka halaman galeri.";
            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(requireActivity(), GaleriActivity.class);
                startActivity(intent);
            });
        } else if (command.contains("info")) {
            response = "Membuka halaman informasi.";
            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(requireActivity(), InfoActivity.class);
                startActivity(intent);
            });
        } else if (command.contains("exit")) {
            response = "Menutup dialog...";
            requireActivity().runOnUiThread(() -> {
                new Handler().postDelayed(() -> {
                    // Mengakses dialog yang sedang aktif melalui getDialog()
                    Dialog dialog = getDialog();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss(); // Menutup dialog
                    }
                }, 2000);
            });
        } else if (command.contains("visi") || command.contains("misi")) {
            response = "Visi kami adalah menjadi pusat unggulan dalam pendidikan dan riset di bidang teknologi informasi.";
        } else if (command.contains("kontak")) {
            response = "Anda dapat menghubungi kami melalui website resmi ti.its.ac.id.";
        } else {
            response = "Maaf, saya tidak mengerti perintah Anda.";
        }
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(requireContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(new Locale("id", "ID"));
                    textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });
        } else {
            textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null);
        }

        resetAutoDismissTimer(); // reset timer setelah memberikan jawaban
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        handler.removeCallbacks(autoDismissRunnable);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissCallback != null) {
            onDismissCallback.run();
        }
    }
}
