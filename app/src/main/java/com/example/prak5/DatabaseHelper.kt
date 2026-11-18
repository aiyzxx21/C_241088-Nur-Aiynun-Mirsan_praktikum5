
package com.example.prak5

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // Membuat tabel ketika database pertama kali dibuat
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_MAHASISWA (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nim TEXT NOT NULL UNIQUE,
                nama TEXT NOT NULL,
                alamat TEXT,
                jurusan TEXT,
                nomor_hp TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
        seedData(db)
    }

    // Memperbarui database
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Tidak menghapus data lama
    }

    // Data awal mahasiswa
    private fun seedData(db: SQLiteDatabase) {
        val seed = listOf(
            Mahasiswa(
                nim = "21001",
                nama = "Ahmad Fauzi",
                alamat = "Jl. Pettarani No. 5",
                jurusan = "Sistem Informasi",
                nomorHp = "081234567890"
            ),
            Mahasiswa(
                nim = "21002",
                nama = "Siti Nurhaliza",
                alamat = "Jl. Perintis Kemerdekaan",
                jurusan = "Sistem Informasi",
                nomorHp = "082198765432"
            ),
            Mahasiswa(
                nim = "21003",
                nama = "Budi Santoso",
                alamat = "Jl. Rappocini No. 12",
                jurusan = "Sistem Informasi",
                nomorHp = "081345678901"
            )
        )

        seed.forEach {
            db.insert(TABLE_MAHASISWA, null, toValues(it))
        }
    }

    // Mengubah data mahasiswa menjadi ContentValues
    private fun toValues(mhs: Mahasiswa): ContentValues {
        return ContentValues().apply {
            put("nim", mhs.nim)
            put("nama", mhs.nama)
            put("alamat", mhs.alamat)
            put("jurusan", mhs.jurusan)
            put("nomor_hp", mhs.nomorHp)
        }
    }

    // Mengubah Cursor menjadi objek Mahasiswa
    private fun toMahasiswa(cursor: Cursor): Mahasiswa {
        return Mahasiswa(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            nim = cursor.getString(cursor.getColumnIndexOrThrow("nim")),
            nama = cursor.getString(cursor.getColumnIndexOrThrow("nama")),
            alamat = cursor.getString(cursor.getColumnIndexOrThrow("alamat")),
            jurusan = cursor.getString(cursor.getColumnIndexOrThrow("jurusan")),
            nomorHp = cursor.getString(cursor.getColumnIndexOrThrow("nomor_hp"))
        )
    }

    // CREATE: Menambahkan mahasiswa
    fun insertMahasiswa(
        nim: String,
        nama: String,
        alamat: String,
        jurusan: String,
        nomorHp: String
    ): Long {
        val values = ContentValues().apply {
            put("nim", nim)
            put("nama", nama)
            put("alamat", alamat)
            put("jurusan", jurusan)
            put("nomor_hp", nomorHp)
        }

        return writableDatabase.insert(
            TABLE_MAHASISWA,
            null,
            values
        )
    }

    // UPDATE: Memperbarui mahasiswa berdasarkan ID
    fun updateMahasiswa(
        id: Int,
        nim: String,
        nama: String,
        alamat: String,
        jurusan: String,
        nomorHp: String
    ): Int {
        val values = ContentValues().apply {
            put("nim", nim)
            put("nama", nama)
            put("alamat", alamat)
            put("jurusan", jurusan)
            put("nomor_hp", nomorHp)
        }

        return writableDatabase.update(
            TABLE_MAHASISWA,
            values,
            "id = ?",
            arrayOf(id.toString())
        )
    }

    // DELETE: Menghapus mahasiswa berdasarkan ID
    fun deleteMahasiswa(id: Int): Int {
        return writableDatabase.delete(
            TABLE_MAHASISWA,
            "id = ?",
            arrayOf(id.toString())
        )
    }

    // Mengecek NIM yang sudah terdaftar
    fun isNimTerdaftar(
        nim: String,
        exceptId: Int? = null
    ): Boolean {
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA,
            arrayOf("id"),
            if (exceptId == null) {
                "nim = ?"
            } else {
                "nim = ? AND id != ?"
            },
            if (exceptId == null) {
                arrayOf(nim)
            } else {
                arrayOf(nim, exceptId.toString())
            },
            null,
            null,
            null
        )

        return cursor.use {
            it.moveToFirst()
        }
    }

    // READ: Mengambil seluruh data mahasiswa
    // false = urut nama, true = urut NIM
    fun getAllMahasiswa(
        urutNim: Boolean = false
    ): List<Mahasiswa> {

        val list = mutableListOf<Mahasiswa>()

        val orderBy = if (urutNim) {
            "nim ASC"
        } else {
            "nama ASC"
        }

        val cursor = readableDatabase.query(
            TABLE_MAHASISWA,
            null,
            null,
            null,
            null,
            null,
            orderBy
        )

        cursor.use {
            while (it.moveToNext()) {
                list.add(toMahasiswa(it))
            }
        }

        return list
    }

    // READ: Mengambil satu mahasiswa berdasarkan ID
    fun getMahasiswaById(id: Int): Mahasiswa? {
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA,
            null,
            "id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                toMahasiswa(it)
            } else {
                null
            }
        }
    }

    companion object {
        private const val DB_NAME = "praktikum.db"
        private const val DB_VERSION = 1
        private const val TABLE_MAHASISWA = "mahasiswa"
    }
}
