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

class ManajerMenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PengajuanAdapter
    private var listPengajuan = ArrayList<PengajuanModel>()

    private lateinit var rvDaftarPengajuan: RecyclerView
    private lateinit var tvKosong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manajer_menu)

        dbHelper = DatabaseHelper(this)

        rvDaftarPengajuan = findViewById(R.id.rvDaftarPengajuan)
        tvKosong = findViewById(R.id.tvKosong)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        rvDaftarPengajuan.layoutManager = LinearLayoutManager(this)
        rvDaftarPengajuan.setHasFixedSize(true)

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        tampilkanDataManajer()
    }

    private fun tampilkanDataManajer() {
        listPengajuan = dbHelper.getAllPengajuan()

        if (listPengajuan.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            rvDaftarPengajuan.visibility = View.GONE
        } else {
            tvKosong.visibility = View.GONE
            rvDaftarPengajuan.visibility = View.VISIBLE

            adapter = PengajuanAdapter(listPengajuan) { itemSelected, kategori ->
                showAksiDialog(itemSelected, kategori)
            }
            rvDaftarPengajuan.adapter = adapter
        }
    }

    private fun showAksiDialog(item: PengajuanModel, kategori: String) {
        val options = arrayOf("Setujui Pengajuan", "Tolak Pengajuan")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Proses Ajuan: ${item.tujuan}")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> prosesUpdateStatus(item.id, "Disetujui", kategori)
                1 -> showInputTolakDialog(item, kategori)
            }
        }
        builder.show()
    }

    private fun showInputTolakDialog(item: PengajuanModel, kategori: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tolak Pengajuan Manajer")
        builder.setMessage("Silakan masukkan alasan penolakan:")

        val inputAlasan = EditText(this)
        inputAlasan.hint = "Contoh: Prioritas operasional rendah / berkas kurang lengkap"
        builder.setView(inputAlasan)

        builder.setPositiveButton("Tolak") { dialog, _ ->
            val alasan = inputAlasan.text.toString().trim()
            if (alasan.isEmpty()) {
                Toast.makeText(this, "Gagal! Alasan penolakan wajib diisi.", Toast.LENGTH_SHORT).show()
            } else {
                prosesUpdateStatus(item.id, "Ditolak", kategori, alasan)
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.setCancelable(false)
        builder.show()
    }

    private fun prosesUpdateStatus(id: Int, status: String, kategori: String, alasan: String = "") {
        val sukses: Boolean = when (kategori.lowercase().trim()) {
            "perjalanan dinas" -> dbHelper.updateStatusDinas(id, status, alasan)
            "logistik barang" -> dbHelper.updateStatusLogistik(id, status, alasan)
            // Tambahkan parameter 'alasan' di dua baris ini:
            "penomoran dokumen" -> dbHelper.updateStatusDokumen(id, status, alasan)
            "pengajuan kendaraan dinas" -> dbHelper.updateStatusKendaraan(id, status, alasan)
            else -> false
        }

        if (sukses) {
            Toast.makeText(this, "Pengajuan berhasil diperbarui menjadi $status!", Toast.LENGTH_SHORT).show()
            tampilkanDataManajer()
        } else {
            Toast.makeText(this, "Gagal memperbarui status pengajuan", Toast.LENGTH_SHORT).show()
        }
    }
}