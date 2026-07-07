package com.example.rekraprima

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class GeneralAffairMenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PengajuanAdapter
    private var listPengajuan = ArrayList<PengajuanModel>()
    private lateinit var rvDaftarPengajuan: RecyclerView
    private lateinit var tvKosong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.genneral_affair_menu)

        dbHelper = DatabaseHelper(this)
        rvDaftarPengajuan = findViewById(R.id.rvDaftarPengajuan)
        tvKosong = findViewById(R.id.tvKosong)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        rvDaftarPengajuan.layoutManager = LinearLayoutManager(this)
        rvDaftarPengajuan.setHasFixedSize(true)

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        tampilkanDataGA()
    }

    private fun tampilkanDataGA() {
        listPengajuan = dbHelper.getGAPengajuan()

        if (listPengajuan.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            rvDaftarPengajuan.visibility = View.GONE
        } else {
            tvKosong.visibility = View.GONE
            rvDaftarPengajuan.visibility = View.VISIBLE

            adapter = PengajuanAdapter(listPengajuan) { itemSelected, kategori ->
                if (itemSelected.status.lowercase().trim() == "surat telah diterbitkan") {
                    Toast.makeText(this, "Surat resmi untuk pengajuan ini sudah diterbitkan!", Toast.LENGTH_SHORT).show()
                } else {
                    showInputSuratDialog(itemSelected, kategori)
                }
            }
            rvDaftarPengajuan.adapter = adapter
        }
    }

    private fun showInputSuratDialog(item: PengajuanModel, kategori: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Terbitkan Surat Resmi")
        builder.setMessage("Masukkan Nomor Surat atau Keterangan Dokumen Pendukung:")

        val inputSurat = EditText(this).apply {
            hint = "Contoh: 042/LB-GA/LRS/VI/2026"
        }
        builder.setView(inputSurat)

        builder.setPositiveButton("Terbitkan") { dialog, _ ->
            val isiSurat = inputSurat.text.toString().trim()
            if (isiSurat.isEmpty()) {
                Toast.makeText(this, "Nomor/Keterangan Surat wajib diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val sukses = if (kategori.lowercase().trim() == "perjalanan dinas") {
                    dbHelper.updateSuratDinas(item.id, isiSurat)
                } else {
                    dbHelper.updateSuratLogistik(item.id, isiSurat)
                }

                if (sukses) {
                    Toast.makeText(this, "Surat resmi berhasil diterbitkan!", Toast.LENGTH_SHORT).show()
                    tampilkanDataGA()
                } else {
                    Toast.makeText(this, "Gagal menerbitkan surat", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}