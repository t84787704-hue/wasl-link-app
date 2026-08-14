package com.example.data

import kotlinx.coroutines.flow.Flow

class ShopRepository(private val shopDao: ShopDao) {
    val shopProfile: Flow<ShopProfile?> = shopDao.getShopProfile()

    suspend fun saveProfile(profile: ShopProfile) {
        shopDao.saveShopProfile(profile)
    }
}
