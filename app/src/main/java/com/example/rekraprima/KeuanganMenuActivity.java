package com.example.rekraprima;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class KeuanganMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PengajuanAdapter adapter;
    private TextView tvKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keuangan_menu);

        recyclerView = findViewById(R.id.rvDaftarPengajuan);
        tvKosong     = findViewById(R.id.tvKosong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton btnKeluar  = findViewById(R.id.btnLogout);
        btnKeluar.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Pengajuan> list = DataStore.getInstance().getDaftarPengajuan();

        if (list.isEmpty()) {
            tvKosong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvKosong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new PengajuanAdapter(list, pengajuan -> {
                Intent intent = getIntentForJenis(pengajuan.jenis);
                intent.putExtra("MODE_MANAGER", true);
                intent.putExtra("PENGAJUAN_ID", pengajuan.id); // kirim ID untuk update status
                intent.putExtra("DATA_PENGAJUAN", pengajuan);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private Intent getIntentForJenis(String jenis) {
        if (jenis.startsWith("Kendaraan Dinas")) {
            return new Intent(this, PengajuanKendaraanDinasActivity.class);
        } else if (jenis.equals("Logistik Barang")) {
            return new Intent(this, LogistikBarangActivity.class);
        } else if (jenis.equals("Penomeran Dokumen")) {
            return new Intent(this, PenomeranDokumenActivity.class);
        } else {
            return new Intent(this, PengajuanPerjalananDinasActivity.class);
        }
    }
}