package com.example.efisherypricelist

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun randomUUID() = UUID.randomUUID().toString()

fun getDate(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(Date())

fun getTimeStamp(): String {
    val tsLong = System.currentTimeMillis() / 1000
    return tsLong.toString()
}

fun Int.toRupiah(): String {
    val format = DecimalFormat("##,###")
    return "Rp. ${format.format(this)}"
}