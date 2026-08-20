package com.example.data

data class CountryCodeOption(
    val code: String, // Digits only, e.g. "966", "92", "1"
    val dialCode: String, // e.g. "+966", "+92", "+1"
    val countryName: String, // e.g. "Saudi Arabia"
    val flag: String // e.g. "🇸🇦"
) {
    val displayName: String
        get() = "$flag $dialCode ($countryName)"

    val shortDisplay: String
        get() = "$flag $dialCode"
}

object CountryCodeHelper {
    const val DEFAULT_COUNTRY_CODE = "966"

    val supportedCountries: List<CountryCodeOption> = listOf(
        CountryCodeOption("966", "+966", "Saudi Arabia", "🇸🇦"),
        CountryCodeOption("92", "+92", "Pakistan", "🇵🇰"),
        CountryCodeOption("91", "+91", "India", "🇮🇳"),
        CountryCodeOption("86", "+86", "China", "🇨🇳"),
        CountryCodeOption("1", "+1", "USA / Canada", "🇺🇸"),
        CountryCodeOption("44", "+44", "UK", "🇬🇧"),
        CountryCodeOption("971", "+971", "UAE", "🇦🇪"),
        CountryCodeOption("90", "+90", "Turkey", "🇹🇷"),
        CountryCodeOption("880", "+880", "Bangladesh", "🇧🇩"),
        CountryCodeOption("20", "+20", "Egypt", "🇪🇬"),
        CountryCodeOption("965", "+965", "Kuwait", "🇰🇼"),
        CountryCodeOption("974", "+974", "Qatar", "🇶🇦"),
        CountryCodeOption("973", "+973", "Bahrain", "🇧🇭"),
        CountryCodeOption("968", "+968", "Oman", "🇴🇲"),
        CountryCodeOption("962", "+962", "Jordan", "🇯🇴"),
        CountryCodeOption("961", "+961", "Lebanon", "🇱🇧"),
        CountryCodeOption("81", "+81", "Japan", "🇯🇵"),
        CountryCodeOption("49", "+49", "Germany", "🇩🇪"),
        CountryCodeOption("33", "+33", "France", "🇫🇷"),
        CountryCodeOption("39", "+39", "Italy", "🇮🇹"),
        CountryCodeOption("34", "+34", "Spain", "🇪🇸"),
        CountryCodeOption("61", "+61", "Australia", "🇦🇺"),
        CountryCodeOption("55", "+55", "Brazil", "🇧🇷"),
        CountryCodeOption("62", "+62", "Indonesia", "🇮🇩"),
        CountryCodeOption("60", "+60", "Malaysia", "🇲🇾"),
        CountryCodeOption("63", "+63", "Philippines", "🇵🇭"),
        CountryCodeOption("65", "+65", "Singapore", "🇸🇬"),
        CountryCodeOption("234", "+234", "Nigeria", "🇳🇬"),
        CountryCodeOption("27", "+27", "South Africa", "🇿🇦"),
        CountryCodeOption("7", "+7", "Russia / Kazakhstan", "🇷🇺"),
        CountryCodeOption("82", "+82", "South Korea", "🇰🇷")
    )

    fun getCountryOption(code: String): CountryCodeOption {
        val cleanCode = code.replace("+", "").trim()
        return supportedCountries.find { it.code == cleanCode }
            ?: supportedCountries.find { it.code == DEFAULT_COUNTRY_CODE }
            ?: supportedCountries.first()
    }

    fun searchCountries(query: String): List<CountryCodeOption> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return supportedCountries
        val cleanQ = q.replace("+", "")
        return supportedCountries.filter { country ->
            country.countryName.lowercase().contains(q) ||
            country.code.contains(cleanQ) ||
            country.dialCode.contains(q)
        }
    }

    fun cleanLocalNumber(number: String, countryCode: String = DEFAULT_COUNTRY_CODE): String {
        var digits = number.replace("+", "").replace(" ", "").replace("-", "").filter { it.isDigit() }
        val cleanCC = countryCode.replace("+", "").trim()
        if (cleanCC.isNotBlank() && digits.startsWith(cleanCC)) {
            digits = digits.removePrefix(cleanCC)
        }
        if (digits.startsWith("0")) {
            digits = digits.removePrefix("0")
        }
        return digits
    }

    fun formatFullInternational(countryCode: String, number: String): String {
        val cleanCC = countryCode.replace("+", "").trim().ifBlank { DEFAULT_COUNTRY_CODE }
        val cleanNum = cleanLocalNumber(number, cleanCC)
        return if (cleanNum.isNotBlank()) "$cleanCC$cleanNum" else ""
    }

    fun formatDisplayInternational(countryCode: String, number: String, fallbackDefault: String = "+966 50 123 4567"): String {
        val cleanCC = countryCode.replace("+", "").trim().ifBlank { DEFAULT_COUNTRY_CODE }
        val cleanNum = cleanLocalNumber(number, cleanCC)
        return if (cleanNum.isNotBlank()) "+$cleanCC $cleanNum" else fallbackDefault
    }
}
