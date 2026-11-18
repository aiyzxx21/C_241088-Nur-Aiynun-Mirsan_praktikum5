
package com.example.prak5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DetailActivity : LoggedActivity() {

    private lateinit var db: DatabaseHelper
    private var mahasiswaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = DatabaseHelper(this)

        mahasiswaId = intent.getIntExtra(EXTRA_ID, -1)

        val btnUbah = findViewById<Button>(R.id.btnUbah)
        val btnHapus = findViewById<Button>(R.id.btnHapus)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        btnUbah.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra(EXTRA_ID, mahasiswaId)
            startActivity(intent)
        }

        btnHapus.setOnClickListener {
            val hasil = db.deleteMahasiswa(mahasiswaId)

            if (hasil > 0) {
                Toast.makeText(
                    this,
                    "Data berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Gagal menghapus data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnKembali.setOnClickListener {
            finish()
        }

        tampilkanDetail()
    }

    private fun tampilkanDetail() {
        val mahasiswa = db.getMahasiswaById(mahasiswaId)

        if (mahasiswa == null) {
            Toast.makeText(
                this,
                "Data mahasiswa tidak ditemukan",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvNim).text = mahasiswa.nim
        findViewById<TextView>(R.id.tvNama).text = mahasiswa.nama
        findViewById<TextView>(R.id.tvAlamat).text = mahasiswa.alamat
        findViewById<TextView>(R.id.tvJurusan).text = mahasiswa.jurusan
        findViewById<TextView>(R.id.tvNomorHp).text = mahasiswa.nomorHp
    }

    override fun onResume() {
        super.onResume()
        if (::db.isInitialized) {
            tampilkanDetail()
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
