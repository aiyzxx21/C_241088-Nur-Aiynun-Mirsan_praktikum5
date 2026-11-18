
package com.example.prak5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class MainActivity : LoggedActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var lvMahasiswa: ListView
    private lateinit var btnTambah: Button
    private lateinit var btnUrut: Button

    private var urutNim: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        lvMahasiswa = findViewById(R.id.lvMahasiswa)
        btnTambah = findViewById(R.id.btnTambah)
        btnUrut = findViewById(R.id.btnUrut)

        // Memulihkan pilihan pengurutan setelah rotasi layar
        urutNim = savedInstanceState?.getBoolean(KEY_URUT_NIM) ?: false

        // Tombol tambah data
        btnTambah.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        // Tombol mengubah urutan daftar
        btnUrut.setOnClickListener {
            urutNim = !urutNim
            tampilkanMahasiswa()
        }

        // Membuka halaman detail mahasiswa
        lvMahasiswa.setOnItemClickListener { _, _, position, _ ->
            val adapter = lvMahasiswa.adapter as MahasiswaAdapter
            val mahasiswa = adapter.getItem(position)

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, mahasiswa.id)
            startActivity(intent)
        }

        tampilkanMahasiswa()
    }

    override fun onResume() {
        super.onResume()
        tampilkanMahasiswa()
    }

    // Menampilkan daftar mahasiswa
    private fun tampilkanMahasiswa() {
        val daftarMahasiswa = ArrayList(db.getAllMahasiswa(urutNim))

        // Logcat Info saat memuat data
        Log.i("Data", "Memuat ${daftarMahasiswa.size} data mahasiswa")

        val adapter = MahasiswaAdapter(this, daftarMahasiswa)
        lvMahasiswa.adapter = adapter

        btnUrut.text = if (urutNim) {
            "Urut: NIM"
        } else {
            "Urut: Nama"
        }

        if (daftarMahasiswa.isEmpty()) {
            Toast.makeText(
                this,
                "Belum ada data mahasiswa",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Menyimpan pilihan urutan sebelum Activity dibuat ulang
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_URUT_NIM, urutNim)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val KEY_URUT_NIM = "key_urut_nim"
    }
}
