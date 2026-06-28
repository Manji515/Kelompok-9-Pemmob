package com.example.rekraprima

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class PengajuanPerjalananDinasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.karyawan_pengajuan_perjalanan_dinas)

        dbHelper = DatabaseHelper(this)

        val txtTujuan = findViewById<TextInputEditText>(R.id.txtJudul)
        val txtTglBerangkat = findViewById<TextInputEditText>(R.id.txtTanggal)
        val txtTglKembali = findViewById<TextInputEditText>(R.id.txtJenis)
        val txtAgenda = findViewById<TextInputEditText>(R.id.txtPerihal)

        val btnKirim = findViewById<MaterialButton>(R.id.btnDaftar)
        val btnKembali = findViewById<MaterialButton>(R.id.btnBatal)

        txtTglBerangkat.isFocusable = false
        txtTglBerangkat.isClickable = true

        txtTglKembali.isFocusable = false
        txtTglKembali.isClickable = true

        txtTglBerangkat.setOnClickListener {
            showDatePickerDialog(txtTglBerangkat)
        }

        txtTglKembali.setOnClickListener {
            showDatePickerDialog(txtTglKembali)
        }

        btnKirim.setOnClickListener {
            val tujuan = txtTujuan.text.toString().trim()
            val tglBerangkat = txtTglBerangkat.text.toString().trim()
            val tglKembali = txtTglKembali.text.toString().trim()
            val agenda = txtAgenda.text.toString().trim()

            if (tujuan.isEmpty() || tglBerangkat.isEmpty() || tglKembali.isEmpty() || agenda.isEmpty()) {
                Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val hasil = dbHelper.insertPengajuan(tujuan, tglBerangkat, tglKembali, agenda)

                if (hasil > -1) {
                    Toast.makeText(this, "Pengajuan dinas berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data ke database", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format hasil tanggal menjadi DD/MM/YYYY (Bulan ditambah 1 karena indeks mulai dari 0)
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}