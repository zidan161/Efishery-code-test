package com.example.efisherypricelist.model

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("province")
    val province: String,

    @SerializedName("city")
    val city: String
)
