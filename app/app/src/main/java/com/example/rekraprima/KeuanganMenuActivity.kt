package com.example.rekraprima

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class KeuanganMenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PengajuanAdapter
    private var listPengajuan = ArrayList<PengajuanModel>()

    private lateinit var rvDaftarPengajuan: RecyclerView
    private lateinit var tvKosong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.keuangan_menu)

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
        tampilkanDataKeuangan()
    }

    private fun tampilkanDataKeuangan() {
        listPengajuan = dbHelper.getKeuanganPengajuan()

        if (listPengajuan.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            rvDaftarPengajuan.visibility = View.GONE
        } else {
            tvKosong.visibility = View.GONE
            rvDaftarPengajuan.visibility = View.VISIBLE

            adapter = PengajuanAdapter(listPengajuan) { itemSelected, kategori ->
                val status = itemSelected.status.lowercase().trim()
                if (status == "anggaran sudah ditentukan" || status == "ditolak" || status == "surat telah diterbitkan") {
                    Toast.makeText(this, "Pengajuan ini sudah selesai diproses Keuangan!", Toast.LENGTH_SHORT).show()
                } else {
                    showAksiDialog(itemSelected, kategori)
                }
            }
            rvDaftarPengajuan.adapter = adapter
        }
    }

    private fun showAksiDialog(item: PengajuanModel, kategori: String) {
        val options = arrayOf("Tentukan Anggaran (Setujui)", "Tolak Pengajuan")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Proses Ajuan: ${item.tujuan}")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showInputAnggaranDialog(item, kategori)
                1 -> showInputTolakDialog(item, kategori) // Diarahkan ke dialog input alasan penolakan
            }
        }
        builder.show()
    }

    private fun showInputAnggaranDialog(item: PengajuanModel, kategori: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tentukan Anggaran")
        builder.setMessage("Masukkan nominal anggaran yang disetujui (Rupiah):")

        val inputAnggaran = EditText(this)
        inputAnggaran.inputType = InputType.TYPE_CLASS_NUMBER
        inputAnggaran.hint = "Contoh: 500000"
        builder.setView(inputAnggaran)

        builder.setPositiveButton("Simpan") { dialog, _ ->
            val nominal = inputAnggaran.text.toString().trim()
            if (nominal.isEmpty()) {
                Toast.makeText(this, "Anggaran tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val statusBaru = "Anggaran Sudah Ditentukan"
                prosesUpdateStatus(item.id, statusBaru, kategori)
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun showInputTolakDialog(item: PengajuanModel, kategori: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tolak Pengajuan Keuangan")
        builder.setMessage("Silakan masukkan alasan penolakan finansial:")

        val inputAlasan = EditText(this)
        inputAlasan.hint = "Contoh: Alokasi dana anggaran penuh / di luar regulasi"
        builder.setView(inputAlasan)

        builder.setPositiveButton("Tolak") { dialog, _ ->
            val alasan = inputAlasan.text.toString().trim()
            if (alasan.isEmpty()) {
                Toast.makeText(this, "Gagal! Pihak Keuangan wajib memberikan alasan penolakan.", Toast.LENGTH_LONG).show()
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
        val sukses: Boolean
        if (kategori.lowercase().trim() == "perjalanan dinas") {
            sukses = dbHelper.updateStatusDinas(id, status, alasan)
        } else {
            sukses = dbHelper.updateStatusLogistik(id, status, alasan)
        }

        if (sukses) {
            Toast.makeText(this, "Status ajuan berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            tampilkanDataKeuangan()
        } else {
            Toast.makeText(this, "Gagal memperbarui status", Toast.LENGTH_SHORT).show()
        }
    }
}