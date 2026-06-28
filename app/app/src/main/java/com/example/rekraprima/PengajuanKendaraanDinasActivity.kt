package com.example.rekraprima

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PengajuanKendaraanDinasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.karyawan_pengajuan_kendaraan_dinas)

        dbHelper = DatabaseHelper(this)

        val spnKendaraan = findViewById<Spinner>(R.id.spnRole)
        val txtTujuan = findViewById<TextInputEditText>(R.id.txtTanggal)
        val txtTanggal = findViewById<TextInputEditText>(R.id.txtJenis)
        val txtAgenda = findViewById<TextInputEditText>(R.id.txtPerihal)

        val btnKirim = findViewById<MaterialButton>(R.id.btnDaftar)
        val btnKembali = findViewById<MaterialButton>(R.id.btnBatal)

        val listKendaraan = arrayOf("Mobil Dinas (Avanza)", "Mobil Box Logistik", "Sepeda Motor Operasional")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listKendaraan)
        spnKendaraan.adapter = adapterSpinner

        txtTanggal.isFocusable = false
        txtTanggal.isClickable = true
        txtTanggal.setOnClickListener {
            showDatePicker(txtTanggal)
        }

        btnKirim.setOnClickListener {
            val jenisKendaraan = spnKendaraan.selectedItem.toString()
            val tujuan = txtTujuan.text.toString().trim()
            val tanggal = txtTanggal.text.toString().trim()
            val agenda = txtAgenda.text.toString().trim()

            if (tujuan.isEmpty() || tanggal.isEmpty() || agenda.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                // Simpan ke database SQLite
                val hasil = dbHelper.insertKendaraan(jenisKendaraan, tujuan, tanggal, agenda)

                if (hasil > -1) {
                    Toast.makeText(this, "Pengajuan kendaraan dinas berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memproses data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formatted = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formatted)
        }, year, month, day).show()
    }
}