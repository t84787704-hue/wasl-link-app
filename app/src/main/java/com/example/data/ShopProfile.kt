package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_profile")
data class ShopProfile(
    @PrimaryKey val id: Int = 1,
    val shopName: String = "Al-Naseem Specialty Coffee",
    val shopNameArabic: String = "محمصة وقهوة النسيم المختصة",
    val whatsappNumber: String = "501234567",
    val locationUrl: String = "https://maps.google.com/?q=Riyadh+Saudi+Arabia",
    val menuItemsText: String = """
        • فلات وايت | Flat White - 18 ر.س
        • كورتادو | Cortado - 16 ر.س
        • قهوة مقطرة V60 إثيوبيا | V60 Drip - 22 ر.س
        • كيكة الزعفران | Saffron Cake - 28 ر.س
        • كرواسون اللوز | Almond Croissant - 16 ر.س
        • بوكس القهوة المقطرة (6 حبات) | Drip Box - 95 ر.س
    """.trimIndent(),
    val logoEmoji: String = "☕",
    val category: String = "مقهى ومخبوزات • Specialty Cafe",
    val city: String = "الرياض • Riyadh",
    val updatedAt: Long = System.currentTimeMillis()
)
