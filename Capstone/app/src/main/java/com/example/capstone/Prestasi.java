package com.example.capstone;
public class Prestasi {
    String nama, nrp, kejuaraan, penyelenggara, juara, tahun;

    public Prestasi(String nama, String nrp, String kejuaraan, String penyelenggara, String juara, String tahun) {
        this.nama = nama;
        this.nrp = nrp;
        this.kejuaraan = kejuaraan;
        this.penyelenggara = penyelenggara;
        this.juara = juara;
        this.tahun = tahun;
    }

    // Getter (optional: bisa juga pakai langsung field public)
    public String getNama() { return nama; }
    public String getNrp() { return nrp; }
    public String getKejuaraan() { return kejuaraan; }
    public String getPenyelenggara() { return penyelenggara; }
    public String getJuara() { return juara; }
    public String getTahun() { return tahun; }
}
