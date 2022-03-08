package com.example.efisherypricelist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Fish(
    @SerializedName("uuid")
    @PrimaryKey
    var id: String,

    @SerializedName("komoditas")
    var name: String,

    @SerializedName("area_provinsi")
    var province: String,

    @SerializedName("area_kota")
    var city: String,

    @SerializedName("size")
    var size: Int,

    @SerializedName("price")
    var price: Int,

    @SerializedName("tgl_parsed")
    var dateParsed: String?,

    @SerializedName("timestamp")
    var timestamp: String?
): Parcelable

data class FishPostResponse(@SerializedName("updatedRange") val updateRange: String)
