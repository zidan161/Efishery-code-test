package com.example.efisherypricelist.data.remote

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.enqueueData(callback: (T) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                callback(response.body() as T)
            } else {
                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
        }
    })
}

fun <T> Call<T>.enqueueDataLive(): LiveData<T> {
    val live = MutableLiveData<T>()
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                live.value = response.body() as T
            } else {
                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
        }
    })
    return live
}