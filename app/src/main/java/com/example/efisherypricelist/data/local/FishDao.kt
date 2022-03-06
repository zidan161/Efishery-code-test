package com.example.efisherypricelist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.efisherypricelist.model.Fish

@Dao
interface FishDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fish: Fish)

    @Update
    fun update(fish: Fish)

    @Delete
    fun delete(fish: Fish)

    @Query("SELECT * from fish ORDER BY dateParsed ASC")
    fun getFish(): LiveData<List<Fish>>

    @Query("SELECT * from fish ORDER BY name")
    fun getFishOrderByName(): LiveData<List<Fish>>

    @Query("SELECT * from fish ORDER BY price")
    fun getFishOrderByPrice(): LiveData<List<Fish>>

    @Query("SELECT * from fish ORDER BY size")
    fun getFishOrderBySize(): LiveData<List<Fish>>
}