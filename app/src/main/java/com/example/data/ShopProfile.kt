package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_profile")
data class ShopProfile(
    @PrimaryKey val id: Int = 1,
    val shopName: String = "",
    val whatsappNumber: String = "",
    val locationUrl: String = "",
    val menuItemsText: String = "",
    val logoEmoji: String = "☕",
    val category: String = "",
    val city: String = "",
    val defaultGreeting: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
