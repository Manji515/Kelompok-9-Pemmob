package com.example.rekraprima

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "RekraPrima.db"
        private const val DATABASE_VERSION = 2 // Naikkan versi karena struktur tabel berubah
        const val TABLE_USERS = "users"
        const val COL_ID = "id"
        const val COL_USERNAME = "username"
        const val COL_PASSWORD = "password"
        const val COL_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_USERNAME TEXT UNIQUE, " +
                "$COL_PASSWORD TEXT, " +
                "$COL_ROLE TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun registerUser(username: String, password: String, role: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_USERNAME, username)
        contentValues.put(COL_PASSWORD, password)
        contentValues.put(COL_ROLE, role)

        val result = db.insert(TABLE_USERS, null, contentValues)

        return result
    }

    fun getUserRole(username: String, password: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $COL_ROLE FROM $TABLE_USERS WHERE $COL_USERNAME = ? AND $COL_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        var role: String? = null

        if (cursor.moveToFirst()) {
            role = cursor.getString(0)
        }

        cursor.close()


        return role
    }
}