package com.example.efisherypricelist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.efisherypricelist.data.local.FishDao
import com.example.efisherypricelist.data.local.MainDatabase
import com.example.efisherypricelist.data.remote.ApiHelper
import com.example.efisherypricelist.data.remote.GetApiWorker
import com.example.efisherypricelist.data.remote.enqueueDataLive
import com.example.efisherypricelist.model.Area
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.model.FishPostResponse
import com.example.efisherypricelist.model.Size

class MainRepository(private val application: Application) {

    private val mFishDao: FishDao
    private val apiHelper = ApiHelper.getApiService()

    init {
        val db = MainDatabase.getDatabase(application)
        mFishDao = db.fishDao()
    }

    fun getPrices(): LiveData<List<Fish>> {
        updatePrices()
        return mFishDao.getFish()
    }

    fun updatePrices() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request: OneTimeWorkRequest = OneTimeWorkRequestBuilder<GetApiWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL, 20,
                java.util.concurrent.TimeUnit.SECONDS
            ).build()

        WorkManager.getInstance(application).enqueue(request)
    }

    fun postPrice(data: Fish): LiveData<FishPostResponse> {
        return apiHelper.postPrice(listOf(data)).enqueueDataLive()
    }

    fun getAllArea(): LiveData<List<Area>> = apiHelper.getArea().enqueueDataLive()

    fun getAllSize(): LiveData<List<Size>> = apiHelper.getSize().enqueueDataLive()
}