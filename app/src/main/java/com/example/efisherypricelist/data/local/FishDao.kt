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

    @Query("SELECT * from fish WHERE id = :id")
    fun getAlreadyFish(id: String): Fish?

    @Query("SELECT * from fish WHERE name = :name")
    fun getFishByName(name: String): LiveData<List<Fish>>

    @Query("SELECT * from fish")
    fun getFish(): LiveData<List<Fish>>
}