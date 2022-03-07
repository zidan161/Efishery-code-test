package com.example.efisherypricelist

import androidx.lifecycle.ViewModel
import com.example.efisherypricelist.data.MainRepository

class AddDataViewModel(private val repository: MainRepository): ViewModel() {

    fun getArea() = repository.getAllArea()

    fun getSize() = repository.getAllSize()
}