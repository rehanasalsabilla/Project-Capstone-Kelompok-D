
package com.example.capstone;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected com.example.capstone.TTSHelper ttsHelper;
    protected com.example.capstone.LLMClient llmClient;
    protected com.example.capstone.SpeechHelper speechHelper;
    protected WakeWordHelper wakeWordHelper;
    protected com.example.capstone.ResponseDialog responseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Jangan panggil initializeSpeechComponents() di sini, panggil dari subclass setelah permission
    }

    protected void initializeSpeechComponents() {
        // Inisialisasi TTS & LLM
        ttsHelper = new com.example.capstone.TTSHelper(this);
        responseDialog = new com.example.capstone.ResponseDialog(this);

        speechHelper = new com.example.capstone.SpeechHelper(this, speech -> {
            runOnUiThread(() -> {
                handleSpeechResult(speech);
                responseDialog.show(speech); // tampilkan dialog saat STT selesai
                llmClient.sendMessage(speech); // kirim ke LLM
            });
        });

        llmClient = new com.example.capstone.LLMClient(this, ttsHelper, response -> {
            runOnUiThread(() -> {
                handleLLMResponse(response);
                if (responseDialog != null) {
                    responseDialog.updateOutput(response); // update jawaban
                }
            });
        });


        // Listener agar wake word bisa diulang setelah STT
        speechHelper.setOnSpeechCompleteListener(() -> {
            runOnUiThread(() -> {
                onSpeechComplete();
                wakeWordHelper.start();
            });
        });

        // Wake Word listener
        wakeWordHelper = new WakeWordHelper(this, () -> {
            runOnUiThread(() -> {
                onWakeWordDetected();
                wakeWordHelper.stop(); // Supaya tidak double trigger
                speechHelper.startListening();
            });
        });

        wakeWordHelper.start();
    }
    protected void setupBackButton() {
        ImageButton backButton = findViewById(R.id.button_back);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

    protected void handleLLMResponse(String response) {
        // Optional: override di subclass jika perlu
    }

    protected void handleSpeechResult(String speech) {
        // Optional: override di subclass
    }

    protected void onWakeWordDetected() {
        // Optional: override di subclass
    }

    protected void onSpeechComplete() {
        // Optional: override di subclass
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (speechHelper != null) {
            speechHelper.handleActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsHelper != null) {
            ttsHelper.shutdown();
        }
        if (wakeWordHelper != null) {
            wakeWordHelper.stop();
        }
    }
}