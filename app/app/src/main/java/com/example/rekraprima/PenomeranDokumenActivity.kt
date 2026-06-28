package com.example.rekraprima

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PenomeranDokumenActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.karyawan_penomeran_dokumen)

        dbHelper = DatabaseHelper(this)

        val txtJudul = findViewById<TextInputEditText>(R.id.txtJudul)
        val txtTanggal = findViewById<TextInputEditText>(R.id.txtTanggal)
        val txtJenis = findViewById<TextInputEditText>(R.id.txtJenis)
        val txtPerihal = findViewById<TextInputEditText>(R.id.txtPerihal)

        val btnKirim = findViewById<MaterialButton>(R.id.btnDaftar)
        val btnKembali = findViewById<MaterialButton>(R.id.btnBatal)

        txtTanggal.isFocusable = false
        txtTanggal.isClickable = true
        txtTanggal.setOnClickListener {
            showDatePicker(txtTanggal)
        }

        btnKirim.setOnClickListener {
            val judul = txtJudul.text.toString().trim()
            val tanggal = txtTanggal.text.toString().trim()
            val jenis = txtJenis.text.toString().trim()
            val perihal = txtPerihal.text.toString().trim()

            if (judul.isEmpty() || tanggal.isEmpty() || jenis.isEmpty() || perihal.isEmpty()) {
                Toast.makeText(this, "Semua formulir wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val hasil = dbHelper.insertDokumen(judul, tanggal, jenis, perihal)

                if (hasil > -1) {
                    Toast.makeText(this, "Pengajuan Nomor Dokumen Berhasil!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memproses dokumen", Toast.LENGTH_SHORT).show()
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

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formatTgl = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formatTgl)
            },
            year, month, day
        ).show()
    }
}