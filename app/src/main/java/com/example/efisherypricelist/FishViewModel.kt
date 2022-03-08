package com.example.efisherypricelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository
import com.example.efisherypricelist.model.Fish

class FishViewModel(private val repository: MainRepository): ViewModel() {

    private var listPrice = mutableListOf<Fish>()

    var prices = MutableLiveData<List<Fish>>()

    fun setData(list: List<Fish>) {
        listPrice.clear()
        listPrice.addAll(list)
        prices.value = listPrice
    }

    fun getPrices() = repository.getPrices()

    fun sortPricesByName() = repository.orderPricesByName()

    fun sortPricesByPrice() = repository.orderPricesByPrice()

    fun sortPricesBySize() = repository.orderPricesBySize()

    fun getPricesByName(name: String) {
        listPrice.filter {
            it.name.lowercase().contains(name.lowercase()) ||
                    it.city.lowercase().contains(name.lowercase()) ||
                    it.province.lowercase().contains(name.lowercase()) ||
                    it.price.toString().contains(name) ||
                    it.size.toString().contains(name)
        }.let {
            prices.value = it
        }
    }
}