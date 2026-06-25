package com.example.rekraprima

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val etNameLogin = findViewById<TextInputEditText>(R.id.etNameLogin)
        val etPasswordLogin = findViewById<TextInputEditText>(R.id.etPasswordLogin)
        val btnRegisterIntent = findViewById<MaterialButton>(R.id.btnBatal) // Tombol Register
        val btnLogin = findViewById<MaterialButton>(R.id.btnDaftar) // Tombol Login

        val dbHelper = DatabaseHelper(this)

        btnLogin.setOnClickListener {
            val username = etNameLogin.text.toString().trim()
            val password = etPasswordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val userRole = dbHelper.getUserRole(username, password)

                if (userRole != null) {
                    Toast.makeText(this, "Login Berhasil sebagai $userRole!", Toast.LENGTH_SHORT).show()

                    val intent = when (userRole) {
                        "Manajer" -> Intent(this, ManajerMenuActivity::class.java)
                        "Karyawan" -> Intent(this, MenuActivity::class.java)
                        else ->  null
                    }

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Nama atau Password Salah!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnRegisterIntent.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}