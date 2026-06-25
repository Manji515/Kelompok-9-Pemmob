package com.example.rekraprima;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class PenomeranDokumenActivity extends AppCompatActivity {

    private TextInputEditText txtJudul, txtTanggal, txtJenis, txtPerihal;
    private MaterialButton btnKirim, btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_penomeran_dokumen);

        txtJudul   = findViewById(R.id.txtJudul);
        txtTanggal = findViewById(R.id.txtTanggal);
        txtJenis   = findViewById(R.id.txtJenis);
        txtPerihal = findViewById(R.id.txtPerihal);
        btnKirim   = findViewById(R.id.btnDaftar);
        btnKembali = findViewById(R.id.btnBatal);

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
        txtTanggal.setFocusable(false);
        txtTanggal.setOnClickListener(v -> showDatePicker(txtTanggal));

        btnKirim.setText("Kirim");
        btnKirim.setOnClickListener(v -> kirim());
    }

    private void kirim() {
        String judul   = getText(txtJudul);
        String tanggal = getText(txtTanggal);
        String jenis   = getText(txtJenis);
        String perihal = getText(txtPerihal);

        if (judul.isEmpty() || tanggal.isEmpty() || jenis.isEmpty() || perihal.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // judul   → tujuan
        // tanggal → tanggalBerangkat
        // jenis   → tanggalKembali
        // perihal → agenda
        DataStore.getInstance().tambahPengajuan(
                "Penomeran Dokumen",
                judul,
                tanggal,
                jenis,
                perihal
        );

        Toast.makeText(this, "Pengajuan berhasil!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ─── Mode baca (dari daftar pengajuan) ───────────────────────────────────

    private void tampilkanModeReadOnly() {
        Pengajuan p = (Pengajuan) getIntent().getSerializableExtra("DATA_PENGAJUAN");
        if (p == null) { finish(); return; }

        txtJudul.setText(p.tujuan);
        txtTanggal.setText(p.tanggalBerangkat);
        txtJenis.setText(p.tanggalKembali);
        txtPerihal.setText(p.agenda);

        txtJudul.setEnabled(false);
        txtTanggal.setEnabled(false);
        txtJenis.setEnabled(false);
        txtPerihal.setEnabled(false);

        btnKirim.setText("Terkirim");
        btnKirim.setEnabled(false);
        btnKirim.setAlpha(0.5f);
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private String getText(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }

    private void showDatePicker(TextInputEditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                target.setText(String.format("%02d/%02d/%04d", day, month + 1, year)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}