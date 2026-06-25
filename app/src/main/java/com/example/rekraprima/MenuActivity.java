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

public class MenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PengajuanAdapter adapter;
    private TextView tvKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_menu);

        // Tombol menu
        MaterialButton btnPerjalanan = findViewById(R.id.btnPengajuanPerjalananDinas);
        MaterialButton btnPenomeran  = findViewById(R.id.btnPenomeranDokumen);
        MaterialButton btnLogistik   = findViewById(R.id.btnLogistikBarang);
        MaterialButton btnKendaraan  = findViewById(R.id.btnPengajuanKendaraanDinas);

        btnPerjalanan.setOnClickListener(v -> {
            Intent intent = new Intent(this, PengajuanPerjalananDinasActivity.class);
            startActivity(intent);
        });

        // Tombol lain bisa diarahkan ke activity masing-masing nanti
        btnPenomeran.setOnClickListener(v -> { /* TODO */ });
        btnLogistik .setOnClickListener(v -> { /* TODO */ });
        btnKendaraan.setOnClickListener(v -> { /* TODO */ });

        // RecyclerView daftar pengajuan
        recyclerView = findViewById(R.id.rvDaftarPengajuan);
        tvKosong     = findViewById(R.id.tvKosong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list setiap kali kembali ke menu (setelah submit)
        List<Pengajuan> list = DataStore.getInstance().getDaftarPengajuan();

        if (list.isEmpty()) {
            tvKosong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvKosong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new PengajuanAdapter(list, pengajuan -> {
                // Klik item → buka form dalam mode read-only
                Intent intent = new Intent(this, PengajuanPerjalananDinasActivity.class);
                intent.putExtra("MODE_READ_ONLY", true);
                intent.putExtra("DATA_PENGAJUAN", pengajuan);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}
