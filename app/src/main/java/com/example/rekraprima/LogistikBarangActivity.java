package com.example.rekraprima;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LogistikBarangActivity extends AppCompatActivity {

    private TextInputEditText txtBarang, txtJumlah, txtAlasan, txtKeterangan;
    private MaterialButton btnKirim, btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_logistik_barang);

        txtBarang      = findViewById(R.id.txtJudul);
        txtJumlah      = findViewById(R.id.txtTanggal);
        txtAlasan      = findViewById(R.id.txtJenis);
        txtKeterangan  = findViewById(R.id.txtPerihal);
        btnKirim       = findViewById(R.id.btnDaftar);
        btnKembali     = findViewById(R.id.btnBatal);

        boolean readOnly = getIntent().getBooleanExtra("MODE_READ_ONLY", false);

        if (readOnly) {
            tampilkanModeReadOnly();
        } else {
            setupModeEdit();
        }

        btnKembali.setOnClickListener(v -> finish());
    }

    // ─── Mode edit (pengajuan baru) ───────────────────────────────────────────

    private void setupModeEdit() {
        btnKirim.setText("Kirim");
        btnKirim.setOnClickListener(v -> kirim());
    }

    private void kirim() {
        String barang     = getText(txtBarang);
        String jumlah     = getText(txtJumlah);
        String alasan     = getText(txtAlasan);
        String keterangan = getText(txtKeterangan);

        if (barang.isEmpty() || jumlah.isEmpty() || alasan.isEmpty() || keterangan.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan ke DataStore
        // barang    → tujuan
        // jumlah    → tanggalBerangkat
        // alasan    → tanggalKembali
        // keterangan → agenda
        DataStore.getInstance().tambahPengajuan(
                "Logistik Barang",
                barang,
                jumlah,
                alasan,
                keterangan
        );

        Toast.makeText(this, "Pengajuan berhasil!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ─── Mode baca (dari daftar pengajuan) ───────────────────────────────────

    private void tampilkanModeReadOnly() {
        Pengajuan p = (Pengajuan) getIntent().getSerializableExtra("DATA_PENGAJUAN");
        if (p == null) { finish(); return; }

        txtBarang.setText(p.tujuan);
        txtJumlah.setText(p.tanggalBerangkat);
        txtAlasan.setText(p.tanggalKembali);
        txtKeterangan.setText(p.agenda);

        txtBarang.setEnabled(false);
        txtJumlah.setEnabled(false);
        txtAlasan.setEnabled(false);
        txtKeterangan.setEnabled(false);

        btnKirim.setText("Terkirim");
        btnKirim.setEnabled(false);
        btnKirim.setAlpha(0.5f);
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private String getText(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }
}
