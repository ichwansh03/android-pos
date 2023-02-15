package com.jrektor.skripsi.outlet.employee

class ItemPegawai {

    var id:Int
    var name: String
    var job: String
    var phone: String
    var email: String
    var pin: Int
    var image: String

    constructor(
        id: Int,
        name: String,
        job: String,
        phone: String,
        email: String,
        pin: Int,
        image: String
    ) {
        this.id = id
        this.name = name
        this.job = job
        this.phone = phone
        this.email = email
        this.pin = pin
        this.image = image
    }
}