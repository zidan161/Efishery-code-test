package com.example.efisherypricelist.data.remote

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.efisherypricelist.data.local.MainDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GetApiWorker(private val context: Context, params: WorkerParameters): Worker(context, params) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun doWork(): Result {

        val db = MainDatabase.getDatabase(context)
        val fishDao = db.fishDao()
        val api = ApiHelper.getApiService()

        return try {
            api.getPrices().enqueueData {
                executorService.execute {
                    for (i in it) {
                        val exist = fishDao.getAlreadyFish(i.id)
                        if (exist == null) {
                            fishDao.insert(i)
                        } else {
                            fishDao.update(i)
                        }
                    }
                }
            }
            Result.success()
        } catch(throwable: Throwable) {
            Result.failure()
        }
    }

    private fun <T> Call<T>.enqueueData(callback: (T) -> Unit) {
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
}