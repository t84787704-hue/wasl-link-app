package com.example.ui

import android.Manifest
import android.app.Activity
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
import com.wasl.saudishop.R
import com.example.data.AppDatabase
import com.example.data.AppPreferences
import com.example.data.ShopProfile
import com.example.data.ShopRepository
import com.example.ui.util.LocaleHelper
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
import android.location.Address
import android.location.Geocoder
import android.provider.Settings
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class WaslUiState(
    val shopName: String = "",
    val whatsappNumber: String = "",
    val defaultGreeting: String = "",
    val locationUrl: String = "",
    val menuItemsText: String = "",
    val logoEmoji: String = "☕",
    val category: String = "",
    val city: String = "",
    val activeTab: Int = 0, // 0 = Form, 1 = Preview, 2 = Settings
    val selectedLanguageCode: String = "system",
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
        loadSavedLanguage()
    }

    private fun loadSavedLanguage() {
        viewModelScope.launch {
            val savedLang = AppPreferences.getSelectedLanguage(getApplication()).firstOrNull() ?: "system"
            _uiState.update { it.copy(selectedLanguageCode = savedLang) }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val savedProfile = repository.shopProfile.firstOrNull()
            if (savedProfile != null) {
                _uiState.update {
                    it.copy(
                        shopName = savedProfile.shopName,
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

    fun onWhatsappNumberChange(value: String) {
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

    fun selectLanguage(languageCode: String, activity: Activity) {
        viewModelScope.launch {
            AppPreferences.saveSelectedLanguage(getApplication(), languageCode)
            _uiState.update { it.copy(selectedLanguageCode = languageCode) }
            LocaleHelper.applyLanguage(activity, languageCode)
            activity.recreate()
        }
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
                shopName = state.shopName.ifBlank { "Wasl Market" },
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
                        whatsappNumber = "567890123",
                        defaultGreeting = "أهلاً بك، أود الاستفسار وطلب دهن العود والعطور الملكية 🌸",
                        locationUrl = "https://maps.google.com/?q=Dammam+Ash-Shati",
                        logoEmoji = "🌸",
                        category = "عطور وبخور • Luxury Perfumes",
                        city = "الدمام • Dammam",
                        menuItemsText = """
                            • عود كمبودي معتق سوبر | Aged Cambodian Oud - 290 ر.س
                            • عطر مسك الختام الملكي (100 مل) | Royal Misk 100ml - 185 ر.س
                            • بخور رقائق لاوسي طبيعي | Natural Laotian Chips - 340 ر.س
                            • عطر هيل ولافندر نيش (50 مل) | Cardamom Lavender - 240 ر.س
                            • مخلط العروس الفاخر | Luxury Bridal Blend - 150 ر.س
                        """.trimIndent(),
                        isSaveSuccess = false
                    )
                }
            }
        }
    }

    // Direct WhatsApp Launcher Action - Opens WhatsApp with pre-filled greeting in type bar
    fun openWhatsApp(context: Context) {
        val url = getWhatsAppLinkWithGreeting()

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.toast_whatsapp_error),
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
                context.getString(R.string.toast_maps_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getCleanPhoneNumber(): String {
        val digits = _uiState.value.whatsappNumber.replace("+", "").replace(" ", "").filter { it.isDigit() }
        return when {
            digits.startsWith("966") -> digits.removePrefix("966")
            digits.startsWith("0") -> digits.removePrefix("0")
            else -> digits
        }
    }

    fun getWhatsAppNumberFull(): String {
        val cleanPhone = getCleanPhoneNumber()
        return if (cleanPhone.isNotBlank()) "966$cleanPhone" else ""
    }

    // Generates wa.me link with encoded custom default greeting
    fun getWhatsAppLinkWithGreeting(): String {
        val state = _uiState.value
        val phoneFull = getWhatsAppNumberFull()
        val formattedNumber = if (phoneFull.isNotBlank()) phoneFull else "966591257059"
        val greetingText = state.defaultGreeting.trim()
        val encodedGreeting = if (greetingText.isNotBlank()) Uri.encode(greetingText) else ""
        return if (encodedGreeting.isNotBlank()) {
            "https://wa.me/$formattedNumber?text=$encodedGreeting"
        } else {
            "https://wa.me/$formattedNumber"
        }
    }

    fun getWhatsAppLink(): String {
        return getWhatsAppLinkWithGreeting()
    }

    fun getMapsLink(): String {
        val state = _uiState.value
        val rawUrl = state.locationUrl.trim()
        val coordsMatch = Regex("([0-9.-]+)\\s*[,\\s]\\s*([0-9.-]+)").find(rawUrl)
        return when {
            rawUrl.startsWith("http://") || rawUrl.startsWith("https://") -> rawUrl
            coordsMatch != null -> {
                val (lat, lng) = coordsMatch.destructured
                "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
            }
            rawUrl.isNotBlank() -> "https://www.google.com/maps/search/?api=1&query=${Uri.encode(rawUrl)}"
            state.city.isNotBlank() -> "https://www.google.com/maps/search/?api=1&query=${Uri.encode(state.city + ", Saudi Arabia")}"
            else -> "https://www.google.com/maps/search/?api=1&query=24.56418,46.87677"
        }
    }

    fun getFormattedPhoneDisplay(): String {
        val cleanPhone = getCleanPhoneNumber()
        return if (cleanPhone.isNotBlank()) "+966 $cleanPhone" else "+966 59 125 7059"
    }

    fun getLocationDisplay(): String {
        val state = _uiState.value
        val raw = state.locationUrl.trim()
        val latMatch = Regex("query=([0-9.-]+),([0-9.-]+)").find(raw)
        if (latMatch != null) {
            val (lat, lng) = latMatch.destructured
            return "$lat, $lng"
        }
        val coordsMatch = Regex("([0-9.-]+),\\s*([0-9.-]+)").find(raw)
        if (coordsMatch != null) {
            val (lat, lng) = coordsMatch.destructured
            return "$lat, $lng"
        }
        return if (state.city.isNotBlank()) state.city else "Location available on QR"
    }

    // Final Share Message: Shop Info + Menu + WhatsApp Link (with pre-filled greeting) + Google Maps Link
    fun getShareMessage(): String {
        val state = _uiState.value
        val nameEn = state.shopName.trim()
        val city = state.city.trim()

        val shopName = nameEn.ifBlank { "My Store" }

        val header = if (city.isNotBlank()) "📍 $shopName - $city" else "📍 $shopName"
        val waLinkWithGreeting = getWhatsAppLinkWithGreeting()
        val mapsLink = getMapsLink()

        val menuRaw = state.menuItemsText.trim()
        val menuSection = if (menuRaw.isNotBlank()) {
            "🍽️ Menu & Prices:\n$menuRaw\n\n"
        } else {
            ""
        }

        return buildString {
            appendLine(header)
            appendLine()
            if (menuSection.isNotBlank()) {
                append(menuSection)
            }
            appendLine("💬 WhatsApp: $waLinkWithGreeting")
            appendLine("🗺️ Location: $mapsLink")
            appendLine()
            append("✨ Created via Wasl Market | وصل")
        }
    }

    // Share Storefront Link - Formatted with working https links
    fun shareStoreLink(context: Context) {
        val shareText = getShareMessage()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, context.getString(R.string.share_chooser_title))
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    }

    // Auto-detect current GPS location
    fun detectCurrentLocation(context: Context) {
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
                context.getString(R.string.toast_location_permission_needed),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Check if GPS / Location services are enabled on the device
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

        if (!isGpsEnabled && !isNetworkEnabled) {
            Toast.makeText(
                context,
                "Please turn on GPS / Location Services",
                Toast.LENGTH_LONG
            ).show()
            try {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (_: Exception) {}
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
                context.getString(R.string.toast_location_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            tryLocationManagerFallback(context)
        }
    }

    private fun tryLocationManagerFallback(context: Context) {
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
                    context.getString(R.string.toast_location_gps_failed),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isDetectingLocation = false) }
            Toast.makeText(
                context,
                context.getString(R.string.toast_location_detect_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun applyDetectedLocation(context: Context, latitude: Double, longitude: Double) {
        val formattedLat = String.format(Locale.US, "%.5f", latitude)
        val formattedLng = String.format(Locale.US, "%.5f", longitude)
        val locationUrl = "https://www.google.com/maps/search/?api=1&query=$formattedLat,$formattedLng"

        _uiState.update {
            it.copy(
                locationUrl = locationUrl,
                isDetectingLocation = false
            )
        }

        saveProfile()

        // Reverse geocode in background to auto-suggest City if empty
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        val address = addresses.firstOrNull()
                        val detectedCityName = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                        if (!detectedCityName.isNullOrBlank() && _uiState.value.city.isBlank()) {
                            _uiState.update { it.copy(city = detectedCityName) }
                            saveProfile()
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val address = addresses?.firstOrNull()
                    val detectedCityName = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                    if (!detectedCityName.isNullOrBlank() && _uiState.value.city.isBlank()) {
                        _uiState.update { it.copy(city = detectedCityName) }
                        saveProfile()
                    }
                }
            } catch (_: Exception) {
                // Geocoding optional
            }
        }

        Toast.makeText(
            context,
            "Location detected: $formattedLat, $formattedLng",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    fun openMapPicker(context: Context) {
        val mapUrl = "https://www.google.com/maps"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Toast.makeText(
                context,
                context.getString(R.string.toast_map_picker_guide),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.toast_maps_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
