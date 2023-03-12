package com.jrektor.skripsi

class GlobalData {

    companion object{
        var BASE_URL: String = "http://192.168.43.8/pos/"
        var email: String = String()

        var ids: Int = 0
        var nameProduct: String = String()
        var priceProduct: Int = 0
        var merkProduct: String = String()
        var stockProduct: Int = 0
        var imageProduct: String = String()
        var descProduct: String = String()

        var idCategory: Int = 0
        var nameCategory: String = String()

        var idPegawai: Int = 0
        var namePegawai: String = String()
        var jobPegawai: String = String()
        var phonePegawai: String = String()
        var emailPegawai: String = String()
        var pinPegawai: Int = 0

        var idOutlet: Int = 0
        var nameOutlet: String = String()
        var addressOutlet: String = String()
        var imageOutlet: String = String()

        var idOrder: Int = 0
        var namePelanggan: String = String()
        var phonePelanggan: String = String()
        var jmlBeli: Int = 0
        var notes: String = String()
        var totalBayar: Int = 0
    }
}