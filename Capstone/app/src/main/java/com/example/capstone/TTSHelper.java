package com.example.capstone;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TTSHelper {

    private static final String TAG = "TTSHelper";
    private TextToSpeech tts;

    public TTSHelper(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("id", "ID"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Bahasa tidak didukung");
                } else {
                    Log.d(TAG, "TTS siap dengan bahasa Indonesia");
                }
            } else {
                Log.e(TAG, "Inisialisasi TTS gagal");
            }
        });
    }

    public void speak(String text) {
        if (tts != null) {
            Log.d(TAG, "Membacakan: " + text);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.e(TAG, "TTS belum terinisialisasi saat mencoba speak()");
        }
    }

    public void shutdown() {
        if (tts != null) {
            Log.d(TAG, "Mematikan TTS");
            tts.stop();
            tts.shutdown();
        }
    }
}
