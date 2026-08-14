package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop_profile WHERE id = 1 LIMIT 1")
    fun getShopProfile(): Flow<ShopProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShopProfile(profile: ShopProfile)
}
