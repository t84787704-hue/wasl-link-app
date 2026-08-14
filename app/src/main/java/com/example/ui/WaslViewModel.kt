package com.example.ui

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ShopProfile
import com.example.data.ShopRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class WaslUiState(
    val shopName: String = "Al-Naseem Specialty Coffee",
    val shopNameArabic: String = "محمصة وقهوة النسيم المختصة",
    val whatsappNumber: String = "501234567",
    val defaultGreeting: String = "السلام عليكم، أود الطلب والاستفسار من متجركم.",
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
    val activeTab: Int = 0, // 0 = Form, 1 = Preview
    val isArabicLayout: Boolean = true,
    val isDarkMode: Boolean = false,
    val useDynamicColor: Boolean = true,
    val showMenuSheet: Boolean = false,
    val showQrSheet: Boolean = false,
    val isDetectingLocation: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isLoading: Boolean = true
)

class WaslViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShopRepository

    private val _uiState = MutableStateFlow(WaslUiState())
    val uiState: StateFlow<WaslUiState> = _uiState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ShopRepository(db.shopDao())
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val savedProfile = repository.shopProfile.firstOrNull()
            if (savedProfile != null) {
                _uiState.update {
                    it.copy(
                        shopName = savedProfile.shopName,
                        shopNameArabic = savedProfile.shopNameArabic,
                        whatsappNumber = savedProfile.whatsappNumber,
                        defaultGreeting = savedProfile.defaultGreeting,
                        locationUrl = savedProfile.locationUrl,
                        menuItemsText = savedProfile.menuItemsText,
                        logoEmoji = savedProfile.logoEmoji,
                        category = savedProfile.category,
                        city = savedProfile.city,
                        isLoading = false
                    )
                }
            } else {
                // Initialize default profile to database
                val defaultProfile = ShopProfile()
                repository.saveProfile(defaultProfile)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onShopNameChange(value: String) {
        _uiState.update { it.copy(shopName = value, isSaveSuccess = false) }
    }

    fun onShopNameArabicChange(value: String) {
        _uiState.update { it.copy(shopNameArabic = value, isSaveSuccess = false) }
    }

    fun onWhatsappNumberChange(value: String) {
        // Filter numeric digits only, limit to reasonable length (e.g. 9 or 10 digits)
        val cleaned = value.filter { it.isDigit() }
        _uiState.update { it.copy(whatsappNumber = cleaned, isSaveSuccess = false) }
    }

    fun onDefaultGreetingChange(value: String) {
        _uiState.update { it.copy(defaultGreeting = value, isSaveSuccess = false) }
    }

    fun onLocationUrlChange(value: String) {
        _uiState.update { it.copy(locationUrl = value, isSaveSuccess = false) }
    }

    fun onMenuItemsTextChange(value: String) {
        _uiState.update { it.copy(menuItemsText = value, isSaveSuccess = false) }
    }

    fun onLogoEmojiChange(emoji: String) {
        _uiState.update { it.copy(logoEmoji = emoji, isSaveSuccess = false) }
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category, isSaveSuccess = false) }
    }

    fun onCityChange(city: String) {
        _uiState.update { it.copy(city = city, isSaveSuccess = false) }
    }

    fun setActiveTab(tabIndex: Int) {
        _uiState.update { it.copy(activeTab = tabIndex) }
    }

    fun toggleLanguage() {
        _uiState.update { it.copy(isArabicLayout = !it.isArabicLayout) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleDynamicColor() {
        _uiState.update { it.copy(useDynamicColor = !it.useDynamicColor) }
    }

    fun setShowMenuSheet(show: Boolean) {
        _uiState.update { it.copy(showMenuSheet = show) }
    }

    fun setShowQrSheet(show: Boolean) {
        _uiState.update { it.copy(showQrSheet = show) }
    }

    fun saveProfile() {
        val state = _uiState.value
        viewModelScope.launch {
            val profile = ShopProfile(
                id = 1,
                shopName = state.shopName.ifBlank { "Wasl Shop" },
                shopNameArabic = state.shopNameArabic.ifBlank { "متجر وصل" },
                whatsappNumber = state.whatsappNumber,
                defaultGreeting = state.defaultGreeting,
                locationUrl = state.locationUrl,
                menuItemsText = state.menuItemsText,
                logoEmoji = state.logoEmoji,
                category = state.category,
                city = state.city,
                updatedAt = System.currentTimeMillis()
            )
            repository.saveProfile(profile)
            _uiState.update { it.copy(isSaveSuccess = true) }
        }
    }

    fun loadSampleTemplate(type: String) {
        when (type) {
            "cafe" -> {
                _uiState.update {
                    it.copy(
                        shopName = "Al-Naseem Specialty Coffee",
                        shopNameArabic = "محمصة وقهوة النسيم المختصة",
                        whatsappNumber = "501234567",
                        defaultGreeting = "السلام عليكم، أود طلب قهوة ومخبوزات من قائمة اليوم ☕",
                        locationUrl = "https://maps.google.com/?q=Riyadh+Al-Olaya",
                        logoEmoji = "☕",
                        category = "مقهى ومخبوزات • Specialty Cafe",
                        city = "الرياض • Riyadh",
                        menuItemsText = """
                            • فلات وايت | Flat White - 18 ر.س
                            • كورتادو | Cortado - 16 ر.س
                            • سبانش لاتيه بارد | Iced Spanish Latte - 22 ر.س
                            • قهوة اليوم V60 | V60 Drip Coffee - 20 ر.س
                            • كيكة التمر بالكراميل | Date Caramel Cake - 26 ر.س
                            • كرواسون الجبن والزعتر | Thyme & Cheese Croissant - 15 ر.س
                        """.trimIndent(),
                        isSaveSuccess = false
                    )
                }
            }
            "bakery" -> {
                _uiState.update {
                    it.copy(
                        shopName = "Loom Artisanal Bakery",
                        shopNameArabic = "مخبز لوم الحرفي",
                        whatsappNumber = "559876543",
                        defaultGreeting = "مرحباً، أود حجز مخبوزات طازجة من مخبز لوم 🥐",
                        locationUrl = "https://maps.google.com/?q=Jeddah+Rawdah",
                        logoEmoji = "🥐",
                        category = "مخبوزات وحلويات • Bakery & Pastries",
                        city = "جدة • Jeddah",
                        menuItemsText = """
                            • خبز الساوردو الريفي | Country Sourdough Loaf - 24 ر.س
                            • دنش التوت والكراميل | Berry Danish - 18 ر.س
                            • كوكيز الشوكولاتة البلجيكية | Belgian Choc Cookie - 14 ر.س
                            • كيكة البابكا بالشوكولاتة | Chocolate Babka - 38 ر.س
                            • فطيرة التفاح بالقرفة | Apple Cinnamon Tart - 22 ر.س
                        """.trimIndent(),
                        isSaveSuccess = false
                    )
                }
            }
            "boutique" -> {
                _uiState.update {
                    it.copy(
                        shopName = "Dar Al-Zain Abayas",
                        shopNameArabic = "دار الزين للعبايات والأزياء",
                        whatsappNumber = "543210987",
                        defaultGreeting = "السلام عليكم، أود الاستفسار عن المقاسات والطلب من تشكيلة العبايات 👗",
                        locationUrl = "https://maps.google.com/?q=Khobar+Corniche",
                        logoEmoji = "👗",
                        category = "أزياء وعبايات • Fashion Boutique",
                        city = "الخبر • Khobar",
                        menuItemsText = """
                            • عباية كريب ياباني كلاسيك | Classic Japanese Crepe - 380 ر.س
                            • عباية لينين صيفية مطرزة | Embroidered Linen Abaya - 420 ر.س
                            • طرحة حرير كوري فاخر | Luxury Korean Silk Shayla - 85 ر.س
                            • قفطان مناسبات راقي | Elegant Occasion Kaftan - 650 ر.س
                        """.trimIndent(),
                        isSaveSuccess = false
                    )
                }
            }
            "perfume" -> {
                _uiState.update {
                    it.copy(
                        shopName = "Tayeb Al-Murjan Perfumes",
                        shopNameArabic = "طيب المرجان للعطور والبخور",
                        whatsappNumber = "567890123",
                        defaultGreeting = "السلام عليكم، أود الاستفسار وطلب عطور ودخون من طيب المرجان 🌸",
                        locationUrl = "https://maps.google.com/?q=Dammam+Saudi+Arabia",
                        logoEmoji = "🌸",
                        category = "عطور ودخون • Luxury Fragrances",
                        city = "الدمام • Dammam",
                        menuItemsText = """
                            • عطر مخلط المرجان الملكي (100 مل) | Royal Murjan - 290 ر.س
                            • دهن عود كلمنتان سوبر | Super Kalimatan Oud - 180 ر.س
                            • رقائق عود موروكي طبيعي | Natural Marouki Chips - 140 ر.س
                            • معطر مفارش مسك الرمان | Musk Pomegranate Mist - 65 ر.س
                        """.trimIndent(),
                        isSaveSuccess = false
                    )
                }
            }
        }
    }

    // WhatsApp Action: Opens WhatsApp chat with +966 number and greeting
    fun openWhatsApp(context: Context) {
        val state = _uiState.value
        val rawNumber = state.whatsappNumber.trim()
        val formattedNumber = if (rawNumber.startsWith("966")) {
            rawNumber
        } else if (rawNumber.startsWith("0")) {
            "966" + rawNumber.substring(1)
        } else {
            "966$rawNumber"
        }

        val greetingText = if (state.defaultGreeting.isNotBlank()) {
            state.defaultGreeting
        } else if (state.isArabicLayout) {
            "السلام عليكم، أود الطلب والاستفسار من ${state.shopNameArabic}."
        } else {
            "Hello, I would like to order and inquire from ${state.shopName}."
        }
        val encodedMessage = Uri.encode(greetingText)
        val url = "https://wa.me/$formattedNumber?text=$encodedMessage"

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                if (state.isArabicLayout) "تعذر فتح تطبيق واتساب ($url)" else "Could not open WhatsApp ($url)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Google Maps Location Action: Opens Google Maps link or searches location
    fun openGoogleMaps(context: Context) {
        val state = _uiState.value
        val rawUrl = state.locationUrl.trim()
        val mapUri = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
            Uri.parse(rawUrl)
        } else if (rawUrl.isNotBlank()) {
            Uri.parse("geo:0,0?q=${Uri.encode(rawUrl)}")
        } else {
            Uri.parse("geo:24.7136,46.6753?q=Riyadh")
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, mapUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                if (state.isArabicLayout) "تعذر فتح الخرائط" else "Could not open Maps",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Share Storefront Link
    fun shareStoreLink(context: Context) {
        val state = _uiState.value
        val greetingText = if (state.defaultGreeting.isNotBlank()) {
            state.defaultGreeting
        } else if (state.isArabicLayout) {
            "السلام عليكم، أود الطلب والاستفسار من ${state.shopNameArabic}."
        } else {
            "Hello, I would like to order and inquire from ${state.shopName}."
        }
        val encodedGreeting = Uri.encode(greetingText)
        val shareText = """
            📍 ${state.shopNameArabic} (${state.shopName})
            ${state.category} - ${state.city}
            
            💬 واتساب: https://wa.me/966${state.whatsappNumber}?text=$encodedGreeting
            🗺️ الخريطة: ${state.locationUrl}
            
            ✨ تم إنشاء الرابط عبر تطبيق وصل (Wasl)
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة صفحة المتجر | Share Shop")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }

    // Auto-detect current GPS location
    fun detectCurrentLocation(context: Context) {
        val isArabic = _uiState.value.isArabicLayout
        val hasFinePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            Toast.makeText(
                context,
                if (isArabic) "يرجى السماح بالوصول للموقع لتحديده تلقائياً" else "Please allow location permission to auto-detect",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        _uiState.update { it.copy(isDetectingLocation = true) }

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    applyDetectedLocation(context, location.latitude, location.longitude)
                } else {
                    // Fallback to last known location
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                        if (lastLoc != null) {
                            applyDetectedLocation(context, lastLoc.latitude, lastLoc.longitude)
                        } else {
                            tryLocationManagerFallback(context)
                        }
                    }.addOnFailureListener {
                        tryLocationManagerFallback(context)
                    }
                }
            }.addOnFailureListener {
                tryLocationManagerFallback(context)
            }
        } catch (e: SecurityException) {
            _uiState.update { it.copy(isDetectingLocation = false) }
            Toast.makeText(
                context,
                if (isArabic) "تم رفض إذن الموقع" else "Location permission denied",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            tryLocationManagerFallback(context)
        }
    }

    private fun tryLocationManagerFallback(context: Context) {
        val isArabic = _uiState.value.isArabicLayout
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val gpsLoc = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLoc = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val fallbackLocation = gpsLoc ?: networkLoc

            if (fallbackLocation != null) {
                applyDetectedLocation(context, fallbackLocation.latitude, fallbackLocation.longitude)
            } else {
                _uiState.update { it.copy(isDetectingLocation = false) }
                Toast.makeText(
                    context,
                    if (isArabic) "تعذر الحصول على إحداثيات الموقع حالياً. تأكد من تفعيل الـ GPS" else "Could not get GPS location. Please check GPS settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isDetectingLocation = false) }
            Toast.makeText(
                context,
                if (isArabic) "تعذر تحديد الموقع. يمكنك اختياره يدوياً عبر الخريطة" else "Could not detect location. You can pick it on Map",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun applyDetectedLocation(context: Context, latitude: Double, longitude: Double) {
        val isArabic = _uiState.value.isArabicLayout
        val formattedLat = String.format(Locale.US, "%.5f", latitude)
        val formattedLng = String.format(Locale.US, "%.5f", longitude)
        val locationUrl = "https://maps.google.com/?q=$formattedLat,$formattedLng"

        _uiState.update {
            it.copy(
                locationUrl = locationUrl,
                isDetectingLocation = false
            )
        }

        // Auto-save the detected location into the Room database
        saveProfile()

        Toast.makeText(
            context,
            if (isArabic) "📍 تم تحديد وحفظ موقعك بنجاح!" else "📍 Current location detected & saved!",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Opens Google Maps so user can search or pick their exact location and copy link
    fun openMapPicker(context: Context) {
        val isArabic = _uiState.value.isArabicLayout
        val mapUrl = "https://www.google.com/maps"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Toast.makeText(
                context,
                if (isArabic) "حدد موقع متجرك وانسخ الرابط ثم الصقه هنا" else "Select your shop location, copy link & paste here",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                if (isArabic) "تعذر فتح الخرائط" else "Could not open Google Maps",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
