# [Android] Penerapan Midtrans Payment Gateway pada Sistem Point of Sales (POS)

Point of Sales (PoS) Teknokrat adalah aplikasi berbasis android yang terintegrasi dengan payment gateway. Latar belakang pengembangan aplikasi ini adalah untuk mempermudah para pegiat UMKM mengelola keuangan serta otomatisasi pembayaran secara digital dan laporan rugi-laba.

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

## Entity Relationship Diagram Sistem
![erd pos](https://github.com/ichwansh03/android-pos/assets/34907490/d2c0d41d-6076-46d5-b6e6-4d1a8b7a57b2)


## Screenshot Aplikasi
![ss-pos1](https://github.com/ichwansh03/android-pos/assets/34907490/ec5f3b73-8673-4d5c-969c-6ccff6e9dd5e)

![ss-pos2](https://github.com/ichwansh03/android-pos/assets/34907490/60ce3d81-8d85-4b7d-b15e-634603ab3460)

## LICENSE
Copyright 2023 Ichwan Sholihin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
