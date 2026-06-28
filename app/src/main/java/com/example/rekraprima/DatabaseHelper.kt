package com.example.rekraprima

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "RekraPrima.db"
        private const val DATABASE_VERSION = 12

        // Tabel Users
        const val TABLE_USERS = "users"
        const val COL_ID = "id"
        const val COL_USERNAME = "username"
        const val COL_PASSWORD = "password"
        const val COL_ROLE = "role"

        // Tabel Pengajuan Perjalanan Dinas
        const val TABLE_PENGAJUAN = "pengajuan_dinas"
        const val COL_ID_PENGAJUAN = "id_pengajuan"
        const val COL_TUJUAN = "tujuan"
        const val COL_TGL_BERANGKAT = "tgl_berangkat"
        const val COL_TGL_KEMBALI = "tgl_kembali"
        const val COL_AGENDA = "agenda"
        const val COL_STATUS = "status"
        const val COL_ALASAN_TOLAK_DINAS = "alasan_ditolak"
        const val COL_SURAT_DINAS = "surat_diterbitkan"

        // Tabel Penomoran Dokumen
        const val TABLE_DOKUMEN = "penomoran_dokumen"
        const val COL_ID_DOKUMEN = "id_dokumen"
        const val COL_JUDUL_DOKUMEN = "judul"
        const val COL_TGL_DOKUMEN = "tanggal"
        const val COL_JENIS_DOKUMEN = "jenis_dokumen"
        const val COL_PERIHAL_DOKUMEN = "perihal"
        const val COL_STATUS_DOKUMEN = "status"
        const val COL_ALASAN_TOLAK_DOKUMEN = "alasan_ditolak"

        // Tabel Logistik Barang
        const val TABLE_LOGISTIK = "logistik_barang"
        const val COL_ID_LOGISTIK = "id_logistik"
        const val COL_NAMA_BARANG = "nama_barang"
        const val COL_JUMLAH_BARANG = "jumlah"
        const val COL_ALASAN_LOGISTIK = "alasan"
        const val COL_KET_LOGISTIK = "keterangan"
        const val COL_STATUS_LOGISTIK = "status"
        const val COL_ALASAN_TOLAK_LOGISTIK = "alasan_ditolak"
        const val COL_SURAT_LOGISTIK = "surat_diterbitkan"

        // Tabel Pengajuan Kendaraan Dinas
        const val TABLE_KENDARAAN = "kendaraan_dinas"
        const val COL_ID_KENDARAAN = "id_kendaraan"
        const val COL_JENIS_KENDARAAN = "jenis_kendaraan"
        const val COL_TUJUAN_KENDARAAN = "tujuan"
        const val COL_TGL_KENDARAAN = "tanggal_berangkat"
        const val COL_AGENDA_KENDARAAN = "agenda"
        const val COL_STATUS_KENDARAAN = "status"
        const val COL_ALASAN_TOLAK_KENDARAAN = "alasan_ditolak"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_USERS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_USERNAME TEXT UNIQUE, $COL_PASSWORD TEXT, $COL_ROLE TEXT)")

        db?.execSQL("CREATE TABLE $TABLE_PENGAJUAN ($COL_ID_PENGAJUAN INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TUJUAN TEXT, $COL_TGL_BERANGKAT TEXT, $COL_TGL_KEMBALI TEXT, $COL_AGENDA TEXT, $COL_STATUS TEXT DEFAULT 'Diproses', $COL_ALASAN_TOLAK_DINAS TEXT DEFAULT '', $COL_SURAT_DINAS TEXT DEFAULT '')")

        db?.execSQL("CREATE TABLE $TABLE_DOKUMEN ($COL_ID_DOKUMEN INTEGER PRIMARY KEY AUTOINCREMENT, $COL_JUDUL_DOKUMEN TEXT, $COL_TGL_DOKUMEN TEXT, $COL_JENIS_DOKUMEN TEXT, $COL_PERIHAL_DOKUMEN TEXT, $COL_STATUS_DOKUMEN TEXT DEFAULT 'Diproses', $COL_ALASAN_TOLAK_DOKUMEN TEXT DEFAULT '')")

        db?.execSQL("CREATE TABLE $TABLE_LOGISTIK ($COL_ID_LOGISTIK INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAMA_BARANG TEXT, $COL_JUMLAH_BARANG TEXT, $COL_ALASAN_LOGISTIK TEXT, $COL_KET_LOGISTIK TEXT, $COL_STATUS_LOGISTIK TEXT DEFAULT 'Diproses', $COL_ALASAN_TOLAK_LOGISTIK TEXT DEFAULT '', $COL_SURAT_LOGISTIK TEXT DEFAULT '')")

        db?.execSQL("CREATE TABLE $TABLE_KENDARAAN ($COL_ID_KENDARAAN INTEGER PRIMARY KEY AUTOINCREMENT, $COL_JENIS_KENDARAAN TEXT, $COL_TUJUAN_KENDARAAN TEXT, $COL_TGL_KENDARAAN TEXT, $COL_AGENDA_KENDARAAN TEXT, $COL_STATUS_KENDARAAN TEXT DEFAULT 'Diproses', $COL_ALASAN_TOLAK_KENDARAAN TEXT DEFAULT '')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PENGAJUAN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_DOKUMEN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGISTIK")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_KENDARAAN")
        onCreate(db)
    }

    // Mengambil seluruh data pengajuan
    fun getAllPengajuan(): ArrayList<PengajuanModel> {
        val list = ArrayList<PengajuanModel>()
        val db = this.readableDatabase

        // Menggabungkan data
        val query = "SELECT $COL_ID_PENGAJUAN AS id, $COL_TUJUAN AS judul, $COL_TGL_BERANGKAT AS info_tgl, $COL_TGL_KEMBALI AS info_opsional, ('Perjalanan Dinas|' || $COL_AGENDA || '###' || $COL_ALASAN_TOLAK_DINAS || '@@@' || $COL_SURAT_DINAS) AS gabung_data, $COL_STATUS AS status_proses FROM $TABLE_PENGAJUAN " +
                "UNION ALL " +
                "SELECT $COL_ID_LOGISTIK AS id, $COL_NAMA_BARANG AS judul, $COL_JUMLAH_BARANG AS info_tgl, $COL_ALASAN_LOGISTIK AS info_opsional, ('Logistik Barang|' || $COL_KET_LOGISTIK || '###' || $COL_ALASAN_TOLAK_LOGISTIK || '@@@' || $COL_SURAT_LOGISTIK) AS gabung_data, $COL_STATUS_LOGISTIK AS status_proses FROM $TABLE_LOGISTIK " +
                "UNION ALL " +
                "SELECT $COL_ID_DOKUMEN AS id, $COL_JUDUL_DOKUMEN AS judul, $COL_TGL_DOKUMEN AS info_tgl, $COL_JENIS_DOKUMEN AS info_opsional, ('Penomoran Dokumen|' || $COL_PERIHAL_DOKUMEN || '###' || $COL_ALASAN_TOLAK_DOKUMEN || '@@@' || '') AS gabung_data, $COL_STATUS_DOKUMEN AS status_proses FROM $TABLE_DOKUMEN " +
                "UNION ALL " +
                "SELECT $COL_ID_KENDARAAN AS id, $COL_TUJUAN_KENDARAAN AS judul, $COL_TGL_KENDARAAN AS info_tgl, $COL_JENIS_KENDARAAN AS info_opsional, ('Pengajuan Kendaraan Dinas|' || $COL_AGENDA_KENDARAAN || '###' || $COL_ALASAN_TOLAK_KENDARAAN || '@@@' || '') AS gabung_data, $COL_STATUS_KENDARAAN AS status_proses FROM $TABLE_KENDARAAN " +
                "ORDER BY id DESC"

        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(PengajuanModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Mengambil data yang dilihat oleh bagian keuangan
    fun getKeuanganPengajuan(): ArrayList<PengajuanModel> {
        val list = ArrayList<PengajuanModel>()
        val db = this.readableDatabase
        val query = "SELECT $COL_ID_PENGAJUAN AS id, $COL_TUJUAN AS judul, $COL_TGL_BERANGKAT AS info_tgl, $COL_TGL_KEMBALI AS info_opsional, ('Perjalanan Dinas|' || $COL_AGENDA || '###' || $COL_ALASAN_TOLAK_DINAS || '@@@' || $COL_SURAT_DINAS) AS gabung_data, $COL_STATUS AS status_proses FROM $TABLE_PENGAJUAN WHERE status_proses IN ('Disetujui', 'Anggaran Sudah Ditentukan', 'Ditolak') " +
                "UNION ALL " +
                "SELECT $COL_ID_LOGISTIK AS id, $COL_NAMA_BARANG AS judul, $COL_JUMLAH_BARANG AS info_tgl, $COL_ALASAN_LOGISTIK AS info_opsional, ('Logistik Barang|' || $COL_KET_LOGISTIK || '###' || $COL_ALASAN_TOLAK_LOGISTIK || '@@@' || $COL_SURAT_LOGISTIK) AS gabung_data, $COL_STATUS_LOGISTIK AS status_proses FROM $TABLE_LOGISTIK WHERE status_proses IN ('Disetujui', 'Anggaran Sudah Ditentukan', 'Ditolak') " +
                "ORDER BY id DESC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do { list.add(PengajuanModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5))) } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Mengambil data khusus general affair
    fun getGAPengajuan(): ArrayList<PengajuanModel> {
        val list = ArrayList<PengajuanModel>()
        val db = this.readableDatabase
        val query = "SELECT $COL_ID_PENGAJUAN AS id, $COL_TUJUAN AS judul, $COL_TGL_BERANGKAT AS info_tgl, $COL_TGL_KEMBALI AS info_opsional, ('Perjalanan Dinas|' || $COL_AGENDA || '###' || $COL_ALASAN_TOLAK_DINAS || '@@@' || $COL_SURAT_DINAS) AS gabung_data, $COL_STATUS AS status_proses FROM $TABLE_PENGAJUAN WHERE status_proses IN ('Anggaran Sudah Ditentukan', 'Surat Telah Diterbitkan') " +
                "UNION ALL " +
                "SELECT $COL_ID_LOGISTIK AS id, $COL_NAMA_BARANG AS judul, $COL_JUMLAH_BARANG AS info_tgl, $COL_ALASAN_LOGISTIK AS info_opsional, ('Logistik Barang|' || $COL_KET_LOGISTIK || '###' || $COL_ALASAN_TOLAK_LOGISTIK || '@@@' || $COL_SURAT_LOGISTIK) AS gabung_data, $COL_STATUS_LOGISTIK AS status_proses FROM $TABLE_LOGISTIK WHERE status_proses IN ('Anggaran Sudah Ditentukan', 'Surat Telah Diterbitkan') " +
                "ORDER BY id DESC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do { list.add(PengajuanModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5))) } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Mengubah status pengajuan
    fun updateStatusDokumen(id: Int, s: String, a: String = ""): Boolean {
        return this.writableDatabase.update(TABLE_DOKUMEN, ContentValues().apply { put(COL_STATUS_DOKUMEN, s); put(COL_ALASAN_TOLAK_DOKUMEN, a) }, "$COL_ID_DOKUMEN = ?", arrayOf(id.toString())) > 0
    }
    fun updateStatusKendaraan(id: Int, s: String, a: String = ""): Boolean {
        return this.writableDatabase.update(TABLE_KENDARAAN, ContentValues().apply { put(COL_STATUS_KENDARAAN, s); put(COL_ALASAN_TOLAK_KENDARAAN, a) }, "$COL_ID_KENDARAAN = ?", arrayOf(id.toString())) > 0
    }
    fun updateStatusDinas(id: Int, s: String, a: String = ""): Boolean {
        return this.writableDatabase.update(TABLE_PENGAJUAN, ContentValues().apply { put(COL_STATUS, s); put(COL_ALASAN_TOLAK_DINAS, a) }, "$COL_ID_PENGAJUAN = ?", arrayOf(id.toString())) > 0
    }
    fun updateStatusLogistik(id: Int, s: String, a: String = ""): Boolean {
        return this.writableDatabase.update(TABLE_LOGISTIK, ContentValues().apply { put(COL_STATUS_LOGISTIK, s); put(COL_ALASAN_TOLAK_LOGISTIK, a) }, "$COL_ID_LOGISTIK = ?", arrayOf(id.toString())) > 0
    }
    fun updateSuratDinas(id: Int, i: String): Boolean {
        return this.writableDatabase.update(TABLE_PENGAJUAN, ContentValues().apply { put(COL_SURAT_DINAS, i); put(COL_STATUS, "Surat Telah Diterbitkan") }, "$COL_ID_PENGAJUAN = ?", arrayOf(id.toString())) > 0
    }
    fun updateSuratLogistik(id: Int, i: String): Boolean {
        return this.writableDatabase.update(TABLE_LOGISTIK, ContentValues().apply { put(COL_SURAT_LOGISTIK, i); put(COL_STATUS_LOGISTIK, "Surat Telah Diterbitkan") }, "$COL_ID_LOGISTIK = ?", arrayOf(id.toString())) > 0
    }
    fun registerUser(u: String, p: String, r: String): Long {
        return this.writableDatabase.insert(TABLE_USERS, null, ContentValues().apply { put(COL_USERNAME, u); put(COL_PASSWORD, p); put(COL_ROLE, r) })
    }
    fun getUserRole(u: String, p: String): String? {
        val c = this.readableDatabase.rawQuery("SELECT $COL_ROLE FROM $TABLE_USERS WHERE $COL_USERNAME = ? AND $COL_PASSWORD = ?", arrayOf(u, p))
        var r: String? = null; if (c.moveToFirst()) r = c.getString(0); c.close(); return r
    }
    fun insertPengajuan(t: String, tb: String, tk: String, a: String): Long {
        return this.writableDatabase.insert(TABLE_PENGAJUAN, null, ContentValues().apply { put(COL_TUJUAN, t); put(COL_TGL_BERANGKAT, tb); put(COL_TGL_KEMBALI, tk); put(COL_AGENDA, a) })
    }
    fun insertDokumen(j: String, t: String, jen: String, p: String): Long {
        return this.writableDatabase.insert(TABLE_DOKUMEN, null, ContentValues().apply { put(COL_JUDUL_DOKUMEN, j); put(COL_TGL_DOKUMEN, t); put(COL_JENIS_DOKUMEN, jen); put(COL_PERIHAL_DOKUMEN, p) })
    }
    fun insertLogistik(n: String, j: String, a: String, k: String): Long {
        return this.writableDatabase.insert(TABLE_LOGISTIK, null, ContentValues().apply { put(COL_NAMA_BARANG, n); put(COL_JUMLAH_BARANG, j); put(COL_ALASAN_LOGISTIK, a); put(COL_KET_LOGISTIK, k) })
    }
    fun insertKendaraan(jk: String, tj: String, tg: String, ag: String): Long {
        return this.writableDatabase.insert(TABLE_KENDARAAN, null, ContentValues().apply { put(COL_JENIS_KENDARAAN, jk); put(COL_TUJUAN_KENDARAAN, tj); put(COL_TGL_KENDARAAN, tg); put(COL_AGENDA_KENDARAAN, ag) })
    }
}