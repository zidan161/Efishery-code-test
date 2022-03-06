package com.example.efisherypricelist

import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository

class FishViewModel(private val repository: MainRepository): ViewModel() {

    fun getPrices() = repository.getPrices()
}