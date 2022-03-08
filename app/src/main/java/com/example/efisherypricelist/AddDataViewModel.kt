package com.example.efisherypricelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.model.FishPostResponse

class AddDataViewModel(private val repository: MainRepository): ViewModel() {

    fun getArea() = repository.getAllArea()

    fun getSize() = repository.getAllSize()

    fun postPrice(name: String, province: String, city: String, size: Int, price: Int): LiveData<FishPostResponse> {
        val fish = Fish(randomUUID(), name, province, city, size, price, getDate(), getTimeStamp())
        println(fish)
        return repository.postPrice(fish)
    }

    fun refreshData() { repository.updatePrices() }
}