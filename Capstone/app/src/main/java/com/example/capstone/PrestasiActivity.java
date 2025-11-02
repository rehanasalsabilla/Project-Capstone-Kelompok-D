package com.example.capstone;

import android.os.Bundle;
import android.database.Cursor;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class PrestasiActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinnerTahun;
    SQLiteHelper dbHelper;
    ArrayList<Prestasi> allPrestasiList = new ArrayList<>();
    PrestasiAdapter adapter;
    ArrayList<String> tahunList = new ArrayList<>(); // untuk spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestasi);
        ImageButton backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(v -> finish());
        spinnerTahun = findViewById(R.id.spinnerTahun);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new SQLiteHelper(this);
        dbHelper.insertFromCSV();

        Cursor cursor = dbHelper.getAllPrestasi();
        try {
            while (cursor.moveToNext()) {
                Prestasi p = new Prestasi(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                allPrestasiList.add(p);
                if (!tahunList.contains(p.getTahun())) {
                    tahunList.add(p.getTahun());
                }
            }
        } finally {
            cursor.close();
        }

        Collections.sort(tahunList); // urutkan tahun

        // Pasang spinner filter tahun
        ArrayAdapter<String> tahunAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tahunList);
        tahunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTahun.setAdapter(tahunAdapter);

        // Saat tahun dipilih
        spinnerTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTahun = tahunList.get(position);
                filterByTahun(selectedTahun);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void filterByTahun(String tahun) {
        ArrayList<Prestasi> filtered = new ArrayList<>();
        for (Prestasi p : allPrestasiList) {
            if (p.getTahun().equals(tahun)) {
                filtered.add(p);
            }
        }
        adapter = new PrestasiAdapter(filtered);
        recyclerView.setAdapter(adapter);
    }
}
