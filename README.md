# [Android] Penerapan Midtrans Payment Gateway pada Sistem Point of Sales (POS)
## Fitur
- Indexing Login User Berdasarkan Outlet
- Kelola Kategori Produk (Tambah, Hapus, Ubah)
- Kelola Produk (Tambah, Hapus, Ubah)
- Keranjang Produk
- Metode Pembayaran Menggunakan Cash dan Cetak Struk
- Metode Pembayaran Menggunakan API Midtrans
- Laporan Transaksi (Harian, Pekanan, Bulanan)
- Kelola Pegawai (Tambah, Hapus, Ubah)
- Kelola Outlet/Cabang Outlet (Tambah, Hapus, Ubah)
- Data Transaksi Pelanggan

#### Implementasi Back-End : [download code](https://github.com/ichwansh03/backend-pos/releases/tag/%23back-endpos)

## Setup Project
* clone project dengan perintah
  ```
  git clone https://github.com/ichwansh03/android-pos.git
  ```
* download file back-end diatas dan ekstrak zip file. Jika menggunakan `XAMPP Control Panel`, letakkan hasil ekstrak didalam folder `htdocs`.
* export file `dbpos.sql`
* pada project `Android`, tambahkan beberapa library pada file `build.gradle (module:app)`
* ubah nilai `BASE_URL` pada file ![GlobalData.kt](https://github.com/ichwansh03/android-pos/blob/master/app/src/main/java/com/jrektor/skripsi/GlobalData.kt). Sesuaikan dengan IP local yang digunakan.
* untuk penerapan payment gateway dapat dibaca selengkapnya pada ![Midtrans Documentation](https://docs.midtrans.com/)

## Usecase Diagram Sistem
![usecase](https://github.com/ichwansh03/android-pos/assets/34907490/30847e0b-34e4-4bc2-9cbb-441e96af0832)

## Screenshot Aplikasi
![ss-pos1](https://github.com/ichwansh03/android-pos/assets/34907490/ec5f3b73-8673-4d5c-969c-6ccff6e9dd5e)

![ss-pos2](https://github.com/ichwansh03/android-pos/assets/34907490/60ce3d81-8d85-4b7d-b15e-634603ab3460)

