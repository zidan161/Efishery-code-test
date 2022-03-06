package com.example.efisherypricelist.data.remote

import com.example.efisherypricelist.model.Area
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.model.Size
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiHelper {

    companion object {

        private const val URL = "https://stein.efishery.com/v1/storages/5e1edf521073e315924ceab4/"

        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    interface ApiService {
        @GET("list")
        fun getPrices(): Call<List<Fish>>

        @GET("option_area")
        fun getArea(): Call<List<Area>>

        @GET("option_size")
        fun getSize(): Call<List<Size>>

//        @FormUrlEncoded
//        @POST("cost")
//        fun postPrices(
//            @Field("komoditas") komoditas: String,
//            @Field("area_provinsi") province: String,
//            @Field("area_kota") city: String,
//            @Field("size") size: Int,
//            @Field("price") price: Int
//        ) : Call<CostResponse>
    }
}