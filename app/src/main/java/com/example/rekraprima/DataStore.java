package com.example.rekraprima;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    private final List<Pengajuan> daftarPengajuan = new ArrayList<>();
    private int counter = 1;

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public List<Pengajuan> getDaftarPengajuan() {
        return daftarPengajuan;
    }

    public String tambahPengajuan(String jenis, String tujuan,
                                  String berangkat, String kembali, String agenda) {
        String id = "PDN-" + String.format("%03d", counter++);
        daftarPengajuan.add(0, new Pengajuan(id, jenis, tujuan, berangkat, kembali, agenda));
        return id;
    }

    /** Dipanggil manajer untuk mengubah status pengajuan berdasarkan ID */
    public void updateStatus(String id, String status) {
        for (Pengajuan p : daftarPengajuan) {
            if (p.id.equals(id)) {
                p.status = status;
                break;
            }
        }
    }
}