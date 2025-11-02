package com.example.capstone;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.util.Log;


public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "prestasi.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE prestasi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama TEXT," +
                "nrp TEXT," +
                "kejuaraan TEXT," +
                "penyelenggara TEXT," +
                "juara TEXT," +
                "tahun TEXT)";
        db.execSQL(createTable); // Inisialisasi tabel
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Jika versi database meningkat, hapus tabel lama dan buat yang baru
        db.execSQL("DROP TABLE IF EXISTS prestasi");
        onCreate(db); // Membuat tabel yang baru
    }

    // Fungsi untuk menyisipkan data dari CSV jika belum dilakukan
    public void insertFromCSV() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("prestasi", null, null); // Hapus semua data lama

        try {
            InputStream is = context.getAssets().open("prestasi.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }

                String[] parts = line.split(",");
                if (parts.length == 6) {
                    ContentValues cv = new ContentValues();
                    cv.put("nama", parts[0].trim());
                    cv.put("nrp", parts[1].trim());
                    cv.put("kejuaraan", parts[2].trim());
                    cv.put("penyelenggara", parts[3].trim());
                    cv.put("juara", parts[4].trim());
                    cv.put("tahun", parts[5].trim());
                    db.insert("prestasi", null, cv);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk mendapatkan semua data prestasi
    public Cursor getAllPrestasi() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM prestasi", null); // Mengambil semua data
    }
}
