package com.example.rekraprima

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LogistikBarangActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.karyawan_logistik_barang)

        dbHelper = DatabaseHelper(this)

        val txtNamaBarang = findViewById<TextInputEditText>(R.id.txtJudul)
        val txtJumlah = findViewById<TextInputEditText>(R.id.txtTanggal)
        val txtAlasan = findViewById<TextInputEditText>(R.id.txtJenis)
        val txtKeterangan = findViewById<TextInputEditText>(R.id.txtPerihal)

        val btnKirim = findViewById<MaterialButton>(R.id.btnDaftar)
        val btnKembali = findViewById<MaterialButton>(R.id.btnBatal)

        btnKirim.setOnClickListener {
            val namaBarang = txtNamaBarang.text.toString().trim()
            val jumlah = txtJumlah.text.toString().trim()
            val alasan = txtAlasan.text.toString().trim()
            val keterangan = txtKeterangan.text.toString().trim()

            if (namaBarang.isEmpty() || jumlah.isEmpty() || alasan.isEmpty() || keterangan.isEmpty()) {
                Toast.makeText(this, "Semua kolom logistik harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val hasil = dbHelper.insertLogistik(namaBarang, jumlah, alasan, keterangan)

                if (hasil > -1) {
                    Toast.makeText(this, "Pengajuan logistik barang terkirim!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal memproses pengajuan logistik", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }
    }
}