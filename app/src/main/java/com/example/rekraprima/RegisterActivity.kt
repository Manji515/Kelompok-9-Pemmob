package com.example.rekraprima

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<TextInputEditText>(R.id.etNameRegister)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordRegister)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPasswordRegister)
        val spinnerRole = findViewById<Spinner>(R.id.spinner)

        val btnKirim = findViewById<MaterialButton>(R.id.btnPenomeranDokumen) // Tombol Kirim
        val btnBatal = findViewById<MaterialButton>(R.id.btnPengajuanPerjalananDinas) // Tombol Batal

        val dbHelper = DatabaseHelper(this)

        val listRole = arrayOf("Karyawan", "Manajer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listRole)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        btnKirim.setOnClickListener {
            val username = etName.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val selectedRole = spinnerRole.selectedItem.toString()

            // Validasi Input kosong
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi kecocokan Password
            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Jalankan simpan ke SQLite jika semua validasi aman
            val result = dbHelper.registerUser(username, password, selectedRole)

            if (result != -1L) {
                Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Registrasi Gagal! Nama mungkin sudah terdaftar.", Toast.LENGTH_LONG).show()
            }
        }


        btnBatal.setOnClickListener {
            finish()
        }
    }
}