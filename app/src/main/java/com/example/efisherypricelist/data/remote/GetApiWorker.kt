package com.example.efisherypricelist.data.remote

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.efisherypricelist.data.local.MainDatabase
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
}