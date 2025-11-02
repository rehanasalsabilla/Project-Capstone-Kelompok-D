package com.example.capstone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ai.picovoice.porcupine.PorcupineManager;

public class WakeWordHelper {
    private PorcupineManager porcupineManager;
    private static final String ACCESS_KEY = BuildConfig.PICOVOICE_ACCESS_KEY;
    private static final String WAKE_WORD_FILE = "hellovira.ppn";

    public WakeWordHelper(Context context, Runnable onWakeWordDetected) {
        try {
            String keywordPath = getFilePathFromAssets(context, WAKE_WORD_FILE);
            String modelPath = getFilePathFromAssets(context, "porcupine_params.pv");
            Log.d("WakeWord", "Keyword file path: " + keywordPath);
            Log.d("WakeWord", "Keyword file size: " + new File(keywordPath).length());
            Log.d("WakeWord", "Model file path: " + modelPath);
            Log.d("WakeWord", "Model file size: " + new File(modelPath).length());
            porcupineManager = new PorcupineManager.Builder()
                    .setAccessKey(ACCESS_KEY)
                    .setKeywordPath(keywordPath)
                    .setModelPath(modelPath)
                    .setSensitivity(0.7f)
                    .build(context, index -> {
                        Log.d("WakeWord", "Wake Word Detected!");
                        onWakeWordDetected.run(); // Panggil aksi ketika wake word terdeteksi
                    });

        } catch (Exception e) {
            Log.e("WakeWord", "Porcupine initialization failed.");
            Log.e("WakeWord", "Exception class: " + e.getClass().getName());
            Log.e("WakeWord", "Message: " + e.getMessage());
            Log.e("WakeWord", "Cause: " + (e.getCause() != null ? e.getCause().toString() : "null"));
            Log.e("WakeWord", "StackTrace: ");
            for (StackTraceElement el : e.getStackTrace()) {
                Log.e("WakeWord", "    at " + el.toString());
            }
        }

    }

    public void start() {
        if (porcupineManager != null) {
            try {
                porcupineManager.start();
                Log.d("WakeWord", "Listening for Wake Word...");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("WakeWord", "Error starting Porcupine: " + e.getMessage());
            }
        }
    }

    public void stop() {
        if (porcupineManager != null) {
            try {
                porcupineManager.stop();
                Log.d("WakeWord", "Stopped Wake Word Detection.");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("WakeWord", "Error stopping Porcupine: " + e.getMessage());
            }
        }
    }

    private String getFilePathFromAssets(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (!file.exists()) {
            try (InputStream inputStream = context.getAssets().open(fileName);
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                Log.e("WakeWord", "Failed to copy asset file: " + e.getMessage());
            }
        }
        return file.getAbsolutePath();
    }


}
