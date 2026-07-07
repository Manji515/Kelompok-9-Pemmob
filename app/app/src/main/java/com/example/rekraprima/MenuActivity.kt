package com.example.rekraprima

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PengajuanAdapter
    private var listPengajuan = ArrayList<PengajuanModel>()

    private lateinit var rvDaftarPengajuan: RecyclerView
    private lateinit var tvKosong: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.karyawan_menu)

        dbHelper = DatabaseHelper(this)

        rvDaftarPengajuan = findViewById(R.id.rvDaftarPengajuan)
        tvKosong = findViewById(R.id.tvKosong)

        val btnPengajuanPerjalananDinas = findViewById<MaterialButton>(R.id.btnPengajuanPerjalananDinas)
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)
        val btnPenomeranDokumen = findViewById<MaterialButton>(R.id.btnPenomeranDokumen)
        val btnLogistikBarang = findViewById<MaterialButton>(R.id.btnLogistikBarang)
        val btnPengajuanKendaraanDinas = findViewById<MaterialButton>(R.id.btnPengajuanKendaraanDinas)

        rvDaftarPengajuan.layoutManager = LinearLayoutManager(this)
        rvDaftarPengajuan.setHasFixedSize(true)

        btnPengajuanPerjalananDinas.setOnClickListener {
            val intent = Intent(this, PengajuanPerjalananDinasActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnPenomeranDokumen.setOnClickListener {
            val intent = Intent(this, PenomeranDokumenActivity::class.java)
            startActivity(intent)
        }
        btnLogistikBarang.setOnClickListener {
            val intent = Intent(this, LogistikBarangActivity::class.java)
            startActivity(intent)
        }
        btnPengajuanKendaraanDinas.setOnClickListener {
            val intent = Intent(this, PengajuanKendaraanDinasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        tampilkanDataPengajuan()
    }

    private fun tampilkanDataPengajuan() {
        listPengajuan = dbHelper.getAllPengajuan()

        if (listPengajuan.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            rvDaftarPengajuan.visibility = View.GONE
        } else {
            tvKosong.visibility = View.GONE
            rvDaftarPengajuan.visibility = View.VISIBLE

            adapter = PengajuanAdapter(listPengajuan)
            rvDaftarPengajuan.adapter = adapter
        }
    }
}