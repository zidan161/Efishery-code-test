package com.example.efisherypricelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.model.Fish

class FishViewModel(private val repository: MainRepository): ViewModel() {

    var prices = MutableLiveData<List<Fish>>()

    fun setData(list: List<Fish>) {
        prices.value = list
    }

    fun getPrices() = repository.getPrices()

    fun sortPricesByName() = repository.orderPricesByName()

    fun sortPricesByPrice() = repository.orderPricesByPrice()

    fun sortPricesBySize() = repository.orderPricesBySize()

    fun getPricesByName(name: String) {
        prices.value?.filter { it.name.contains(name) }.let {
            prices.value = it
        }
    }
}