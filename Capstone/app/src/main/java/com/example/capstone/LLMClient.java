package com.example.capstone;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

public class LLMClient {

    private final Context context;
    private final com.example.capstone.TTSHelper ttsHelper;
    private final Consumer<String> responseCallback;

    private final String url = "http://10.4.89.48:5000/api/chat";

    public LLMClient(Context context, com.example.capstone.TTSHelper ttsHelper, Consumer<String> callback) {
        this.context = context;
        this.ttsHelper = ttsHelper;
        this.responseCallback = callback;
    }

    public void sendMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            Toast.makeText(context, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject payload = new JSONObject();
        try {
            payload.put("message", message);
            payload.put("role", "general");
            payload.put("sessionId", "user123");
        } catch (JSONException e) {
            Log.e("LLMClient", "Gagal membuat payload JSON", e);
            Toast.makeText(context, "Error: Gagal membuat payload", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("LLMClient", "Mengirim request ke: " + url);
        Log.d("LLMClient", "Payload yang dikirim: " + payload.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload,
                response -> {
                    try {
                        String reply = response.getString("answer");
                        Log.d("LLMClient", "Diterima balasan: " + reply);
                        ttsHelper.speak(reply);
                        responseCallback.accept(reply);
                    } catch (JSONException e) {
                        Log.e("LLMClient", "Gagal parsing JSON response", e);
                        Toast.makeText(context, "Gagal membaca balasan", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("LLMClient", "Volley error: " + error.toString(), error);
                    try {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String reply = data.getString("answer");
                            Log.d("LLMClient", "Error response dengan answer: " + reply);
                            ttsHelper.speak(reply);
                            responseCallback.accept(reply);
                        } else {
                            throw new Exception("Tidak ada data dari server");
                        }
                    } catch (Exception e) {
                        String fallbackMessage = "Error: " + (error.getMessage() != null ? error.getMessage() : "Tidak diketahui");
                        Toast.makeText(context, fallbackMessage, Toast.LENGTH_LONG).show();
                        Log.d("LLMClient", "Error details: " + fallbackMessage);
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Volley.newRequestQueue(context).add(request);
    }
}
