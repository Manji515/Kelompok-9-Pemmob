package com.example.rekraprima;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class PengajuanPerjalananDinasActivity extends AppCompatActivity {

    private TextInputEditText txtTujuan, txtKeberangkatan, txtKembali, txtAgenda;
    private MaterialButton btnKirim, btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_pengajuan_perjalanan_dinas);

        // Sambungkan view — sesuaikan ID dengan yang ada di XML kamu
        txtTujuan        = findViewById(R.id.txtJudul);
        txtKeberangkatan = findViewById(R.id.txtTanggal);
        txtKembali       = findViewById(R.id.txtJenis);
        txtAgenda        = findViewById(R.id.txtPerihal);

        // PERHATIAN: di XML kamu ID tombol Kirim = btnPenomeranDokumen
        //            dan Kembali = btnPengajuanPerjalananDinas.
        //            Sebaiknya ganti ID-nya di XML menjadi btnKirim & btnKembali
        //            agar tidak membingungkan. Tapi kalau belum sempat, pakai ID lama:
        btnKirim   = findViewById(R.id.btnDaftar);   // ganti jadi btnPenomeranDokumen kalau belum ubah XML
        btnKembali = findViewById(R.id.btnBatal); // ganti jadi btnPengajuanPerjalananDinas kalau belum ubah XML

        // Cek apakah dibuka dalam mode baca (dari daftar pengajuan)
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
        // Date picker untuk tanggal keberangkatan
        txtKeberangkatan.setFocusable(false);
        txtKeberangkatan.setOnClickListener(v -> showDatePicker(txtKeberangkatan));

        // Date picker untuk tanggal kembali
        txtKembali.setFocusable(false);
        txtKembali.setOnClickListener(v -> showDatePicker(txtKembali));

        btnKirim.setText("Kirim");
        btnKirim.setOnClickListener(v -> kirim());
    }

    private void kirim() {
        String tujuan    = getString(txtTujuan);
        String berangkat = getString(txtKeberangkatan);
        String kembali   = getString(txtKembali);
        String agenda    = getString(txtAgenda);

        // Validasi field tidak boleh kosong
        if (tujuan.isEmpty() || berangkat.isEmpty() || kembali.isEmpty() || agenda.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan ke DataStore
        DataStore.getInstance().tambahPengajuan(
                "Perjalanan Dinas", tujuan, berangkat, kembali, agenda);

        // Tampilkan pesan berhasil
        Toast.makeText(this, "Pengajuan berhasil!", Toast.LENGTH_SHORT).show();

        // Kembali ke menu (activity ini selesai)
        finish();
    }

    // ─── Mode baca (dari daftar pengajuan) ───────────────────────────────────

    private void tampilkanModeReadOnly() {
        Pengajuan p = (Pengajuan) getIntent().getSerializableExtra("DATA_PENGAJUAN");
        if (p == null) { finish(); return; }

        // Isi teks
        txtTujuan.setText(p.tujuan);
        txtKeberangkatan.setText(p.tanggalBerangkat);
        txtKembali.setText(p.tanggalKembali);
        txtAgenda.setText(p.agenda);

        // Nonaktifkan semua field agar tidak bisa diedit
        txtTujuan.setEnabled(false);
        txtKeberangkatan.setEnabled(false);
        txtKembali.setEnabled(false);
        txtAgenda.setEnabled(false);

        // Ubah tombol Kirim menjadi Terkirim (abu-abu, tidak bisa diklik)
        btnKirim.setText("Terkirim");
        btnKirim.setEnabled(false);
        btnKirim.setAlpha(0.5f);
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private String getString(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }

    private void showDatePicker(TextInputEditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            // Format: DD/MM/YYYY
            target.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
}

