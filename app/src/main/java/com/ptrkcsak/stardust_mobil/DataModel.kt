package com.ptrkcsak.stardust_mobil

class DataModel {

    var name: String? = null
    var country: String? = null
    var city: String? = null
    var imgURL: String? = null

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getCountrys(): String {
        return country.toString()
    }

    fun setCountrys(country: String) {
        this.country = country
    }

    fun getCitys(): String {
        return city.toString()
    }

    fun setCitys(city: String) {
        this.city = city
    }

    fun getimgURLs(): String {
        return imgURL.toString()
    }

    fun setimgURLs(imgURL: String) {
        this.imgURL = imgURL
    }

}