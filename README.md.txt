# PRAKTIKUM 05
## LIFECYCLE, STATE & LOGCAT

### A. Identitas
- Nama: Nur Aiynun Mirsan
- NIM: 241088
- Program Studi: Sistem Informasi
- Universitas: Universitas Dipa Makassar
- Praktikum: 05

### B. Tujuan Praktikum
Praktikum ini bertujuan memahami siklus hidup Activity pada Android, mengamati callback melalui Logcat, memahami perubahan konfigurasi layar, serta menerapkan penyimpanan state menggunakan Bundle.

### C. Hasil Pengujian

#### 1. Pengujian Lifecycle
Pengujian dilakukan dengan membuka MainActivity, masuk ke DetailActivity, kemudian kembali ke halaman utama.

Hasil:
- MainActivity menjalankan onPause() ketika DetailActivity dibuka.
- DetailActivity menjalankan onCreate(), onStart(), dan onResume().
- Saat kembali, DetailActivity menjalankan onPause(), onStop(), dan onDestroy().
- MainActivity kembali aktif melalui onStart() dan onResume().

#### 2. Pengujian Rotasi Layar
Pengujian dilakukan dengan mengubah urutan mahasiswa menjadi berdasarkan NIM, kemudian memutar layar emulator.

Hasil:
Pilihan pengurutan tetap bertahan setelah rotasi karena nilai urutNim disimpan menggunakan onSaveInstanceState() dan dipulihkan kembali melalui Bundle.

#### 3. Pengujian Logcat
Pengujian dilakukan menggunakan filter tag:Lifecycle dan tag:Data.

Hasil:
- Lifecycle: menampilkan callback aktivitas MainActivity, DetailActivity, dan FormActivity.
- Info: menampilkan pesan saat data mahasiswa dimuat dan berhasil disimpan.
- Warning: menampilkan pesan ketika NIM yang dimasukkan sudah terdaftar.

### D. Jawaban Pertanyaan

**1. Callback apa saja yang berjalan pada MainActivity saat DetailActivity dibuka, dan mengapa onDestroy() tidak termasuk?**

MainActivity menjalankan onPause() dan kemudian onStop() ketika DetailActivity dibuka dan menutupi halaman utama. onDestroy() tidak dijalankan karena MainActivity tidak dihancurkan, melainkan hanya berada dalam kondisi berhenti sementara.

**2. Mengapa EditText pada form pulih sendiri setelah rotasi, sedangkan urutNim tidak?**

EditText dapat memulihkan teks secara otomatis karena memiliki ID pada layout sehingga Android dapat menyimpan dan mengembalikan kondisinya. Sementara itu, urutNim merupakan variabel Kotlin biasa sehingga perlu disimpan secara manual menggunakan onSaveInstanceState() dan dipulihkan melalui Bundle.

**3. Apa perbedaan data yang cocok disimpan di Bundle dan data yang harus disimpan di database?**

Bundle digunakan untuk menyimpan sementara kondisi Activity, seperti pilihan pengurutan. Database digunakan untuk menyimpan data aplikasi secara permanen, seperti NIM, nama, alamat, jurusan, dan nomor HP mahasiswa.

### E. Kesimpulan
Berdasarkan pengujian, aplikasi berhasil menerapkan pencatatan Lifecycle menggunakan Logcat, mempertahankan pilihan pengurutan setelah rotasi layar, serta menampilkan pesan Info dan Warning pada aktivitas pengelolaan data mahasiswa.

### F. Dokumentasi
Screenshot hasil praktikum disimpan dalam folder Screenshot pada direktori proyek.