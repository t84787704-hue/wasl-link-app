package com.example.data

/**
 * Translations and language-specific text helpers for Wasl Market.
 * Ensures that when a specific language (English, Arabic, Urdu, etc.) is active,
 * all templates, strings, categories, cities, greetings, and menu items strictly match
 * that language without mixing.
 */
object TranslationHelper {

    /**
     * Resolves the effective language code ("en", "ar", "ur", "zh", "hi")
     * from the selected language setting. If "system", falls back to "en" or "ar" depending on device default.
     */
    fun getEffectiveLanguage(selectedLanguageCode: String): String {
        return when (selectedLanguageCode.lowercase()) {
            "en" -> "en"
            "ar" -> "ar"
            "ur" -> "ur"
            "zh" -> "zh"
            "hi" -> "hi"
            else -> {
                val sysLang = java.util.Locale.getDefault().language
                if (sysLang == "ar" || sysLang == "ur" || sysLang == "zh" || sysLang == "hi") sysLang else "en"
            }
        }
    }

    /**
     * Filters a raw text or menu line containing pipe separators (e.g. "فلات وايت | Flat White")
     * or mixed language parts so that only the selected language portion is displayed.
     */
    fun displayMenuItem(rawItem: String, lang: String): String {
        if (rawItem.isBlank()) return ""
        val trimmed = rawItem.trim()
        
        // Handle pipe format: "Arabic | English" or "Urdu | English" etc.
        if (trimmed.contains("|")) {
            val parts = trimmed.split("|").map { it.trim() }
            if (parts.size >= 2) {
                return when (lang) {
                    "en" -> {
                        // Return the part that contains Latin characters or the second part
                        val englishPart = parts.firstOrNull { containsLatin(it) } ?: parts.last()
                        englishPart
                    }
                    "ar" -> {
                        // Return the part that contains Arabic characters or the first part
                        val arabicPart = parts.firstOrNull { containsArabic(it) } ?: parts.first()
                        arabicPart
                    }
                    "ur" -> {
                        val urduPart = parts.firstOrNull { containsArabic(it) } ?: parts.first()
                        urduPart
                    }
                    else -> parts.first()
                }
            }
        }

        // Handle bullet points + separator
        return trimmed
    }

    /**
     * Filters the city and category strings if they contain " • " or "|" separators
     */
    fun filterBilingualText(rawText: String, lang: String): String {
        if (rawText.isBlank()) return ""
        if (rawText.contains(" • ")) {
            val parts = rawText.split(" • ").map { it.trim() }
            return when (lang) {
                "en" -> parts.firstOrNull { containsLatin(it) } ?: parts.last()
                "ar", "ur" -> parts.firstOrNull { containsArabic(it) } ?: parts.first()
                else -> if (lang == "en") parts.last() else parts.first()
            }
        }
        if (rawText.contains("|")) {
            val parts = rawText.split("|").map { it.trim() }
            return when (lang) {
                "en" -> parts.firstOrNull { containsLatin(it) } ?: parts.last()
                "ar", "ur" -> parts.firstOrNull { containsArabic(it) } ?: parts.first()
                else -> if (lang == "en") parts.last() else parts.first()
            }
        }
        return rawText
    }

    private fun containsArabic(text: String): Boolean {
        return text.any { it in '\u0600'..'\u06FF' || it in '\u0750'..'\u077F' || it in '\u08A0'..'\u08FF' || it in '\uFB50'..'\uFDFF' || it in '\uFE70'..'\uFEFF' }
    }

    private fun containsLatin(text: String): Boolean {
        return text.any { it in 'a'..'z' || it in 'A'..'Z' }
    }

    /**
     * Preset Templates by Language
     */
    data class ShopPreset(
        val shopName: String,
        val whatsappNumber: String,
        val whatsappCountryCode: String,
        val defaultGreeting: String,
        val locationUrl: String,
        val logoEmoji: String,
        val category: String,
        val city: String,
        val currency: String,
        val menuItemsText: String
    )

    fun getPreset(type: String, lang: String): ShopPreset {
        return when (lang) {
            "en" -> getEnglishPreset(type)
            "ur" -> getUrduPreset(type)
            else -> getArabicPreset(type)
        }
    }

    private fun getEnglishPreset(type: String): ShopPreset {
        return when (type) {
            "cafe" -> ShopPreset(
                shopName = "Al-Naseem Specialty Coffee",
                whatsappNumber = "501234567",
                whatsappCountryCode = "966",
                defaultGreeting = "Hello, I would like to order coffee and fresh pastries from the menu ☕",
                locationUrl = "https://maps.google.com/?q=Riyadh+Al-Olaya",
                logoEmoji = "☕",
                category = "Specialty Cafe & Pastries",
                city = "Riyadh",
                currency = "SAR",
                menuItemsText = """
                    • Flat White - 18 SAR
                    • Cortado - 16 SAR
                    • Iced Spanish Latte - 22 SAR
                    • V60 Drip Coffee - 20 SAR
                    • Date Caramel Cake - 26 SAR
                    • Thyme & Cheese Croissant - 15 SAR
                """.trimIndent()
            )
            "bakery" -> ShopPreset(
                shopName = "Loom Artisanal Bakery",
                whatsappNumber = "559876543",
                whatsappCountryCode = "966",
                defaultGreeting = "Hello, I would like to order fresh artisanal bakery items 🥐",
                locationUrl = "https://maps.google.com/?q=Jeddah+Rawdah",
                logoEmoji = "🥐",
                category = "Bakery & Desserts",
                city = "Jeddah",
                currency = "SAR",
                menuItemsText = """
                    • Country Sourdough Loaf - 24 SAR
                    • Berry Danish - 18 SAR
                    • Belgian Choc Cookie - 14 SAR
                    • Chocolate Babka - 38 SAR
                    • Apple Cinnamon Tart - 22 SAR
                """.trimIndent()
            )
            "boutique" -> ShopPreset(
                shopName = "Dar Al-Zain Abayas",
                whatsappNumber = "543210987",
                whatsappCountryCode = "966",
                defaultGreeting = "Hello, I would like to inquire about sizes and boutique collection 👗",
                locationUrl = "https://maps.google.com/?q=Khobar+Corniche",
                logoEmoji = "👗",
                category = "Fashion & Abayas Boutique",
                city = "Khobar",
                currency = "SAR",
                menuItemsText = """
                    • Classic Japanese Crepe Abaya - 380 SAR
                    • Embroidered Linen Abaya - 420 SAR
                    • Luxury Korean Silk Shayla - 85 SAR
                    • Elegant Occasion Kaftan - 650 SAR
                """.trimIndent()
            )
            "perfume" -> ShopPreset(
                shopName = "Tayeb Al-Murjan Perfumes",
                whatsappNumber = "567890123",
                whatsappCountryCode = "966",
                defaultGreeting = "Hello, I would like to inquire about aged Oud oils and niche perfumes 🌸",
                locationUrl = "https://maps.google.com/?q=Dammam+Ash-Shati",
                logoEmoji = "🌸",
                category = "Luxury Perfumes & Oud",
                city = "Dammam",
                currency = "SAR",
                menuItemsText = """
                    • Aged Cambodian Oud - 290 SAR
                    • Royal Misk 100ml - 185 SAR
                    • Natural Laotian Agarwood Chips - 340 SAR
                    • Cardamom & Lavender Niche 50ml - 240 SAR
                    • Luxury Bridal Blend - 150 SAR
                """.trimIndent()
            )
            else -> getEnglishPreset("cafe")
        }
    }

    private fun getArabicPreset(type: String): ShopPreset {
        return when (type) {
            "cafe" -> ShopPreset(
                shopName = "مقهى النسيم المختص",
                whatsappNumber = "501234567",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام عليكم، أود طلب قهوة ومخبوزات من قائمة اليوم ☕",
                locationUrl = "https://maps.google.com/?q=Riyadh+Al-Olaya",
                logoEmoji = "☕",
                category = "مقهى ومخبوزات مختصة",
                city = "الرياض",
                currency = "SAR",
                menuItemsText = """
                    • فلات وايت - 18 ر.س
                    • كورتادو - 16 ر.س
                    • سبانش لاتيه بارد - 22 ر.س
                    • قهوة اليوم V60 - 20 ر.س
                    • كيكة التمر بالكراميل - 26 ر.س
                    • كرواسون الجبن والزعتر - 15 ر.س
                """.trimIndent()
            )
            "bakery" -> ShopPreset(
                shopName = "مخبز لوم الحرفي",
                whatsappNumber = "559876543",
                whatsappCountryCode = "966",
                defaultGreeting = "مرحباً، أود حجز مخبوزات طازجة من مخبز لوم 🥐",
                locationUrl = "https://maps.google.com/?q=Jeddah+Rawdah",
                logoEmoji = "🥐",
                category = "مخبوزات وحلويات",
                city = "جدة",
                currency = "SAR",
                menuItemsText = """
                    • خبز الساوردو الريفي - 24 ر.س
                    • دنش التوت والكراميل - 18 ر.س
                    • كوكيز الشوكولاتة البلجيكية - 14 ر.س
                    • كيكة البابكا بالشوكولاتة - 38 ر.س
                    • فطيرة التفاح بالقرفة - 22 ر.س
                """.trimIndent()
            )
            "boutique" -> ShopPreset(
                shopName = "دار الزين للعبايات",
                whatsappNumber = "543210987",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام عليكم، أود الاستفسار عن المقاسات والطلب من تشكيلة العبايات 👗",
                locationUrl = "https://maps.google.com/?q=Khobar+Corniche",
                logoEmoji = "👗",
                category = "أزياء وعبايات راقية",
                city = "الخبر",
                currency = "SAR",
                menuItemsText = """
                    • عباية كريب ياباني كلاسيك - 380 ر.س
                    • عباية لينين صيفية مطرزة - 420 ر.س
                    • طرحة حرير كوري فاخر - 85 ر.س
                    • قفطان مناسبات راقي - 650 ر.س
                """.trimIndent()
            )
            "perfume" -> ShopPreset(
                shopName = "طيب المرجان للعطور",
                whatsappNumber = "567890123",
                whatsappCountryCode = "966",
                defaultGreeting = "أهلاً بك، أود الاستفسار وطلب دهن العود والعطور الملكية 🌸",
                locationUrl = "https://maps.google.com/?q=Dammam+Ash-Shati",
                logoEmoji = "🌸",
                category = "عطور وبخور ملكي",
                city = "الدمام",
                currency = "SAR",
                menuItemsText = """
                    • عود كمبودي معتق سوبر - 290 ر.س
                    • عطر مسك الختام الملكي (100 مل) - 185 ر.س
                    • بخور رقائق لاوسي طبيعي - 340 ر.س
                    • عطر هيل ولافندر نيش (50 مل) - 240 ر.س
                    • مخلط العروس الفاخر - 150 ر.س
                """.trimIndent()
            )
            else -> getArabicPreset("cafe")
        }
    }

    private fun getUrduPreset(type: String): ShopPreset {
        return when (type) {
            "cafe" -> ShopPreset(
                shopName = "النسیم اسپیشلٹی کافی",
                whatsappNumber = "501234567",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام علیکم، مجھے مینو سے کافی اور بیکری اشیاء کا آرڈر دینا ہے ☕",
                locationUrl = "https://maps.google.com/?q=Riyadh+Al-Olaya",
                logoEmoji = "☕",
                category = "اسپیشلٹی کیفے اور بیکری",
                city = "ریاض",
                currency = "SAR",
                menuItemsText = """
                    • فلیٹ وائٹ - 18 ریال
                    • کورٹادو - 16 ریال
                    • آئسڈ ہسپانوی لاٹے - 22 ریال
                    • V60 ڈرپ کافی - 20 ریال
                    • کھجور کیریمل کیک - 26 ریال
                    • زیتون و پنیر کروسانٹ - 15 ریال
                """.trimIndent()
            )
            "bakery" -> ShopPreset(
                shopName = "لوم بیکری",
                whatsappNumber = "559876543",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام علیکم، مجھے تازہ بیکری آئٹمز کا آرڈر کرنا ہے 🥐",
                locationUrl = "https://maps.google.com/?q=Jeddah+Rawdah",
                logoEmoji = "🥐",
                category = "بیکری اور میٹھی اشیاء",
                city = "جدہ",
                currency = "SAR",
                menuItemsText = """
                    • روایتی ساورڈو بریڈ - 24 ریال
                    • بیری ڈینش پیسٹری - 18 ریال
                    • بیلجیئم چاکلیٹ کوکیز - 14 ریال
                    • چاکلیٹ بابکا کیک - 38 ریال
                    • ایپل سنامن ٹارٹ - 22 ریال
                """.trimIndent()
            )
            "boutique" -> ShopPreset(
                shopName = "دار الزین عبایہ بوٹیک",
                whatsappNumber = "543210987",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام علیکم، مجھے عبایہ کے سائز اور کلیکشن کے بارے میں معلوم کرنا ہے 👗",
                locationUrl = "https://maps.google.com/?q=Khobar+Corniche",
                logoEmoji = "👗",
                category = "فیشن اور پرتعیش عبایہ",
                city = "الخبر",
                currency = "SAR",
                menuItemsText = """
                    • کلاسک جاپانی کریپ عبایہ - 380 ریال
                    • کڑھائی والا لینن عبایہ - 420 ریال
                    • لگژری سلک شائلہ - 85 ریال
                    • پروقار تقریباتی قفطان - 650 ریال
                """.trimIndent()
            )
            "perfume" -> ShopPreset(
                shopName = "طیب المرجان پرفیومز",
                whatsappNumber = "567890123",
                whatsappCountryCode = "966",
                defaultGreeting = "السلام علیکم، مجھے خالص عود اور شاہی پرفیومز کے بارے میں جاننا ہے 🌸",
                locationUrl = "https://maps.google.com/?q=Dammam+Ash-Shati",
                logoEmoji = "🌸",
                category = "پرتعیش عود اور خوشبوئیں",
                city = "دمام",
                currency = "SAR",
                menuItemsText = """
                    • کمبوڈین پرانا عود - 290 ریال
                    • شاہی مسک الختام (100 ملی لیٹر) - 185 ریال
                    • قدرتی لاؤشین بخور چپس - 340 ریال
                    • الائچی و لیونڈر پرفیوم (50 ملی لیٹر) - 240 ریال
                    • پرتعیش برائیڈل بلینڈ - 150 ریال
                """.trimIndent()
            )
            else -> getUrduPreset("cafe")
        }
    }
}
