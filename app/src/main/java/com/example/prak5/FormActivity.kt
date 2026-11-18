
package com.example.prak5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class FormActivity : LoggedActivity() {

    private lateinit var db: DatabaseHelper
    private var mahasiswaId: Int = -1

    private lateinit var etNim: EditText
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etJurusan: EditText
    private lateinit var etNomorHp: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        db = DatabaseHelper(this)

        etNim = findViewById(R.id.etNim)
        etNama = findViewById(R.id.etNama)
        etAlamat = findViewById(R.id.etAlamat)
        etJurusan = findViewById(R.id.etJurusan)
        etNomorHp = findViewById(R.id.etNomorHp)

        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnBatal = findViewById<Button>(R.id.btnBatal)

        mahasiswaId = intent.getIntExtra(EXTRA_ID, -1)

        btnBatal.setOnClickListener {
            finish()
        }

        if (mahasiswaId != -1) {
            title = "Ubah Data Mahasiswa"
            tampilkanDataLama()
        } else {
            title = "Tambah Data Mahasiswa"
        }

        btnSimpan.setOnClickListener {
            simpanData()
        }
    }

    private fun tampilkanDataLama() {
        val mhs = db.getMahasiswaById(mahasiswaId)

        if (mhs == null) {
            Toast.makeText(
                this,
                "Data tidak ditemukan",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        etNim.setText(mhs.nim)
        etNama.setText(mhs.nama)
        etAlamat.setText(mhs.alamat)
        etJurusan.setText(mhs.jurusan)
        etNomorHp.setText(mhs.nomorHp)
    }

    private fun simpanData() {

        val nim = etNim.text.toString().trim()
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val jurusan = etJurusan.text.toString().trim()
        val nomorHp = etNomorHp.text.toString().trim()

        // 1. Validasi semua kolom wajib diisi
        if (nim.isEmpty()) {
            etNim.error = "NIM wajib diisi"
            etNim.requestFocus()
            return
        }

        if (nama.isEmpty()) {
            etNama.error = "Nama wajib diisi"
            etNama.requestFocus()
            return
        }

        if (alamat.isEmpty()) {
            etAlamat.error = "Alamat wajib diisi"
            etAlamat.requestFocus()
            return
        }

        if (jurusan.isEmpty()) {
            etJurusan.error = "Jurusan wajib diisi"
            etJurusan.requestFocus()
            return
        }

        if (nomorHp.isEmpty()) {
            etNomorHp.error = "Nomor HP wajib diisi"
            etNomorHp.requestFocus()
            return
        }

        // 2. NIM hanya boleh berisi angka
        if (!nim.matches(Regex("^[0-9]+$"))) {
            etNim.error = "NIM hanya boleh berisi angka"
            etNim.requestFocus()
            return
        }

        // 3. NIM minimal 5 digit
        if (nim.length < 5) {
            etNim.error = "NIM minimal 5 digit"
            etNim.requestFocus()
            return
        }

        // 4. Nomor HP hanya boleh berisi angka
        if (!nomorHp.matches(Regex("^[0-9]+$"))) {
            etNomorHp.error = "Nomor HP hanya boleh berisi angka"
            etNomorHp.requestFocus()
            return
        }

        // 5. Nomor HP harus diawali 08 dan 10-13 digit
        if (!nomorHp.matches(Regex("^08[0-9]{8,11}$"))) {
            etNomorHp.error =
                "Nomor HP harus diawali 08 dan terdiri dari 10-13 digit"
            etNomorHp.requestFocus()
            return
        }

        // 6. Validasi NIM ganda
        // Saat mengubah data, NIM milik baris sendiri tetap diperbolehkan.
        val editId = if (mahasiswaId == -1) 0 else mahasiswaId

        if (db.isNimTerdaftar(nim, editId)) {
            Log.w("Data", "NIM $nim sudah terdaftar")
            etNim.error = "NIM sudah terdaftar"
            etNim.requestFocus()
            return
        }

        // 7. Proses tambah atau ubah data
        val berhasil = if (mahasiswaId == -1) {
            db.insertMahasiswa(
                nim,
                nama,
                alamat,
                jurusan,
                nomorHp
            ) != -1L
        } else {
            db.updateMahasiswa(
                mahasiswaId,
                nim,
                nama,
                alamat,
                jurusan,
                nomorHp
            ) > 0
        }

        // 8. Log hasil penyimpanan
        if (berhasil) {

            val aksi = if (mahasiswaId == -1) {
                "ditambahkan"
            } else {
                "diperbarui"
            }

            Log.i("Data", "Data $aksi: nim=$nim")

            Toast.makeText(
                this,
                "Data berhasil $aksi",
                Toast.LENGTH_SHORT
            ).show()

            finish()

        } else {

            Log.e("Data", "Gagal menyimpan nim=$nim")

            Toast.makeText(
                this,
                "Gagal menyimpan data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
