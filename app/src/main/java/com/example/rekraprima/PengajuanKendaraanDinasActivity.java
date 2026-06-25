package com.example.rekraprima;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class PengajuanKendaraanDinasActivity extends AppCompatActivity {

    private Spinner spnKendaraan;
    private TextInputEditText txtKeberangkatan, txtTanggal, txtAgenda;
    private MaterialButton btnKirim, btnKembali;

    // Pilihan jenis kendaraan di spinner
    private final String[] JENIS_KENDARAAN = {
            "Pilih Kendaraan", "Mobil", "Motor", "Bus"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_pengajuan_kendaraan_dinas);

        // Sambungkan view
        spnKendaraan     = findViewById(R.id.spnRole);
        txtKeberangkatan = findViewById(R.id.txtTanggal);
        txtTanggal       = findViewById(R.id.txtJenis);
        txtAgenda        = findViewById(R.id.txtPerihal);
        btnKirim         = findViewById(R.id.btnDaftar);
        btnKembali       = findViewById(R.id.btnBatal);

        // Isi spinner dengan daftar kendaraan
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, JENIS_KENDARAAN);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKendaraan.setAdapter(spinnerAdapter);

        // Cek mode (normal / read-only dari daftar pengajuan)
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
        txtTanggal.setFocusable(false);
        txtTanggal.setOnClickListener(v -> showDatePicker(txtTanggal));

        btnKirim.setText("Kirim");
        btnKirim.setOnClickListener(v -> kirim());
    }

    private void kirim() {
        String kendaraan  = spnKendaraan.getSelectedItem().toString();
        String tujuan     = getText(txtKeberangkatan);
        String tanggal    = getText(txtTanggal);
        String agenda     = getText(txtAgenda);

        // Validasi
        if (kendaraan.equals("Pilih Kendaraan")) {
            Toast.makeText(this, "Pilih jenis kendaraan terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tujuan.isEmpty() || tanggal.isEmpty() || agenda.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simpan ke DataStore
        // tujuan keberangkatan → field tujuan
        // tanggal              → field tanggalBerangkat
        // jenis kendaraan      → field tanggalKembali (dipakai ulang sebagai label)
        DataStore.getInstance().tambahPengajuan(
                "Kendaraan Dinas (" + kendaraan + ")",
                tujuan,
                tanggal,
                kendaraan,  // disimpan di tanggalKembali sementara
                agenda
        );

        Toast.makeText(this, "Pengajuan berhasil!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // ─── Mode baca (dari daftar pengajuan) ───────────────────────────────────

    private void tampilkanModeReadOnly() {
        Pengajuan p = (Pengajuan) getIntent().getSerializableExtra("DATA_PENGAJUAN");
        if (p == null) { finish(); return; }

        // Isi field
        txtKeberangkatan.setText(p.tujuan);
        txtTanggal.setText(p.tanggalBerangkat);
        txtAgenda.setText(p.agenda);

        // Pilih item spinner sesuai yang tersimpan (di tanggalKembali)
        for (int i = 0; i < JENIS_KENDARAAN.length; i++) {
            if (JENIS_KENDARAAN[i].equals(p.tanggalKembali)) {
                spnKendaraan.setSelection(i);
                break;
            }
        }

        // Nonaktifkan semua input
        spnKendaraan.setEnabled(false);
        txtKeberangkatan.setEnabled(false);
        txtTanggal.setEnabled(false);
        txtAgenda.setEnabled(false);

        // Ubah tombol Kirim → Terkirim
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