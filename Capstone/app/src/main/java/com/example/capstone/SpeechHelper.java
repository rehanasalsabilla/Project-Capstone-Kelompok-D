package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class SpeechHelper {

    private static final int REQUEST_CODE = 100;
    private final Activity activity;
    private final Consumer<String> resultCallback;

    private OnSpeechCompleteListener speechCompleteListener;

    public SpeechHelper(Activity activity, Consumer<String> resultCallback) {
        this.activity = activity;
        this.resultCallback = resultCallback;
    }

    // Memulai proses speech recognition
    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");

        try {
            activity.startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(activity, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    // Menangani hasil dari speech recognition
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                resultCallback.accept(result.get(0));  // Kirim hasil ke LLM
            }
        }

        // Beritahu bahwa proses speech recognition sudah selesai
        if (speechCompleteListener != null) {
            speechCompleteListener.onSpeechComplete();
        }
    }

    // Interface untuk callback saat STT selesai
    public interface OnSpeechCompleteListener {
        void onSpeechComplete();
    }

    // Setter untuk listener
    public void setOnSpeechCompleteListener(OnSpeechCompleteListener listener) {
        this.speechCompleteListener = listener;
    }
}
