package com.jrektor.skripsi

class GlobalData {

    companion object{

        const val REQUEST_PERMISSION = 100
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_REQUEST = 2

        var BASE_URL: String = "http://192.168.43.8/pos/"
        var email: String = String()

        var ids: Int = 0
        var nameProduct: String = String()
        var priceProduct: Int = 0
        var merkProduct: String = String()
        var stockProduct: Int = 0
        var quantity: Int = 0
        var imageProduct: String = String()
        var descProduct: String = String()

        var idCategory: Int = 0
        var nameCategory: String = String()

        var idPegawai: Int = 0
        var branchPegawai: String = String()
        var jobPegawai: String = String()
        var idOutlet: Int = 0
        var nameOutlet: String = String()
        var addressOutlet: String = String()
        var imageOutlet: String = String()

        var totalBayar: Int = 0
        var jmlBayarUser: Int = 0
        var jumlahBeli: Int = 0
    }
}