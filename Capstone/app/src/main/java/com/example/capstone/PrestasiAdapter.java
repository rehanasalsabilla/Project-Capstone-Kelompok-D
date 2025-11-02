package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PrestasiAdapter extends RecyclerView.Adapter<PrestasiAdapter.PrestasiViewHolder> {
    ArrayList<Prestasi> list;

    public PrestasiAdapter(ArrayList<Prestasi> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PrestasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prestasi, parent, false);
        return new PrestasiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestasiViewHolder holder, int position) {
        Prestasi p = list.get(position);
        holder.nama.setText(p.getNama());
        holder.nrp.setText(p.getNrp());
        holder.kejuaraan.setText(p.getKejuaraan());
        holder.penyelenggara.setText(p.getPenyelenggara());
        holder.juara.setText(p.getJuara());
        holder.tahun.setText(p.getTahun());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PrestasiViewHolder extends RecyclerView.ViewHolder {
        TextView nama, nrp, kejuaraan, penyelenggara, juara, tahun;

        public PrestasiViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews
            nama = itemView.findViewById(R.id.tvNama);
            nrp = itemView.findViewById(R.id.tvNRP);
            kejuaraan = itemView.findViewById(R.id.tvKejuaraan);
            penyelenggara = itemView.findViewById(R.id.tvPenyelenggara);
            juara = itemView.findViewById(R.id.tvJuara);
            tahun = itemView.findViewById(R.id.tvTahun);
        }
    }
}
