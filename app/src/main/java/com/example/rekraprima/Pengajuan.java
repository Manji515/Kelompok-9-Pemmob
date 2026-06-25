package com.example.rekraprima;

import java.io.Serializable;

public class Pengajuan implements Serializable {
    public String id;
    public String jenis;
    public String status;
    public String tujuan;
    public String tanggalBerangkat;
    public String tanggalKembali;
    public String agenda;

    public Pengajuan(String id, String jenis, String tujuan,
                     String tanggalBerangkat, String tanggalKembali, String agenda) {
        this.id       = id;
        this.jenis    = jenis;
        this.status   = "Proses";
        this.tujuan   = tujuan;
        this.tanggalBerangkat = tanggalBerangkat;
        this.tanggalKembali   = tanggalKembali;
        this.agenda   = agenda;
    }
}