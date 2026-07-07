# Kelompok-9-Pemrograman Mobile I

Len Rekaprima Semesta Operations Hub

# 📝Deskripsi Singkat

Len Rekaprima Semesta Operations Hub adalah aplikasi berbasis mobile yang dirancang untuk mengotomatisasi manajemen administrasi dan pengajuan operasional internal di PT Len Rekaprima Semesta. Aplikasi ini mengintegrasikan empat fitur utama dengan sistem persetujuan bertingkat secara real-time:

- **Perjalanan Dinas & Logistik Barang:** Alur pengajuan terintegrasi mulai dari pengajuan karyawan, persetujuan Manajer, validasi anggaran oleh pihak Keuangan, hingga penerbitan surat resmi oleh General Affair (GA).
- **Penomoran Dokumen & Kendaraan Dinas:** Sistem pengajuan langsung yang menjembatani kebutuhan karyawan dengan validasi instan (ACC/Tolak) oleh Manajer.

# 👥Daftar Anggota kelompok

- **Firman Satrio Aji** - 24552011022 - Frontend Developer
- **Maulana Yusuf Syawaludin** - 24552011072 - Backend Developer
- **Dwitama Andhika Wijaya** - 24552011006

# 🔗Link Video Penjelasan

[Video Penjelasan](https://youtu.be/ZmnIixYFb68)

# 📱Screenshot aplikasi

![image alt](https://github.com/Manji515/Kelompok-9-Pemmob/blob/19fc2189012df9bf2a3ff55a7844a29c4bb69aa4/mockup.png)

# ♣️Cara menjalankan/Cloning proyek

Cara cloning:

- Buka CMD dan masuk ke folder yang akan menjadi tempat clone
- Salin link repository github
- git clone pada CMD dengan cara git clone [tempel_link]
- lalu CD nama project
- Pada CMD ketik git checkout patch-0, karena file berada di branch patch-0
  Cara menjalankan di andorid studio:
- Pada android studio klik file
- Lalu open project
- Pilih folder yang tadi menjadi tempat cloning
- Lalu tunggu hingga gradle selesai
- Jika gradle sudah selesai running aplikasi dengan cara aktifkan emulator
- lalu klik tombol play hijau di bagian atas emulator

Cara mengguanakan aplikasi:

- Registrasi terlebih dahulu dengan membuat Nama, Password, dan Role
- Jika sudah membuat akun lalu login
- Jika Login berhasil user akan diarahkan ke masing masing menu sesuai role
- Role karyawan, dapat mengajukan perjalanan dinas, logistik barang, pengajuan penomeran dokumen, dan pengajuan kendaraan dinas
- Role Manajer, dapat menyetujui atau menolak laporan dari pengajuan karyawan
- Role Keuangan, akan menampilkan pengajuan perjalanan dinas & logistik barang yang sudah disetujui atau ditolak oleh manajer. Jika status nya disetujui, keuangan dapat memberikan anggaran
- Role General Affair, dapat menerbitkan surat pada pengajuan yang telah diberi anggaran oleh keuangan
