package com.example.data

data class CurrencyOption(
    val code: String,
    val symbol: String,
    val nameEn: String,
    val flag: String = ""
) {
    val displayName: String
        get() = "$code $symbol ($nameEn)"
}

object CurrencyHelper {
    val defaultCurrencyCode: String = "SAR"

    val supportedCurrencies: List<CurrencyOption> = listOf(
        CurrencyOption(code = "SAR", symbol = "﷼", nameEn = "Saudi Riyal", flag = "🇸🇦"),
        CurrencyOption(code = "USD", symbol = "$", nameEn = "US Dollar", flag = "🇺🇸"),
        CurrencyOption(code = "PKR", symbol = "Rs", nameEn = "Pakistani Rupee", flag = "🇵🇰"),
        CurrencyOption(code = "INR", symbol = "₹", nameEn = "Indian Rupee", flag = "🇮🇳"),
        CurrencyOption(code = "CNY", symbol = "¥", nameEn = "Chinese Yuan", flag = "🇨🇳"),
        CurrencyOption(code = "EUR", symbol = "€", nameEn = "Euro", flag = "🇪🇺"),
        CurrencyOption(code = "GBP", symbol = "£", nameEn = "British Pound", flag = "🇬🇧"),
        CurrencyOption(code = "AED", symbol = "AED", nameEn = "UAE Dirham", flag = "🇦🇪"),
        CurrencyOption(code = "TRY", symbol = "₺", nameEn = "Turkish Lira", flag = "🇹🇷"),
        CurrencyOption(code = "BDT", symbol = "৳", nameEn = "Bangladeshi Taka", flag = "🇧🇩")
    )

    fun getCurrencySymbol(code: String): String {
        return when (code.trim().uppercase()) {
            "USD" -> "$"
            "PKR" -> "Rs"
            "INR" -> "₹"
            "CNY" -> "¥"
            "EUR" -> "€"
            "GBP" -> "£"
            "AED" -> "AED"
            "TRY" -> "₺"
            "BDT" -> "৳"
            "SAR" -> "﷼"
            else -> if (code.isNotBlank()) code else "﷼"
        }
    }

    fun getCurrencyOption(code: String): CurrencyOption {
        return supportedCurrencies.find { it.code.equals(code, ignoreCase = true) }
            ?: supportedCurrencies.first()
    }

    fun formatPrice(priceRaw: String, currencyCode: String): String {
        val symbol = getCurrencySymbol(currencyCode)
        val trimmed = priceRaw.trim()
        if (trimmed.isEmpty()) return ""

        // Match numeric part with optional decimal (e.g. "18", "18.50", "20 SAR", "SAR 20", "1,500")
        val regex = Regex("""(\d+(?:[.,]\d+)?)""")
        val match = regex.find(trimmed)
        return if (match != null) {
            val amount = match.value
            "$symbol $amount"
        } else {
            "$symbol $trimmed"
        }
    }
}
