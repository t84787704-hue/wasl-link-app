package com.example.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {

    val supportedLanguages = listOf(
        LanguageOption(code = "system", labelRes = "language_system_default", nativeName = "System Default / لغة النظام"),
        LanguageOption(code = "en", labelRes = "language_en", nativeName = "English"),
        LanguageOption(code = "ar", labelRes = "language_ar", nativeName = "العربية"),
        LanguageOption(code = "zh", labelRes = "language_zh", nativeName = "中文 (简体)"),
        LanguageOption(code = "ur", labelRes = "language_ur", nativeName = "اردو"),
        LanguageOption(code = "hi", labelRes = "language_hi", nativeName = "हिंदी")
    )

    fun applyLanguage(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(Context.LOCALE_SERVICE) as? LocaleManager
            if (localeManager != null) {
                if (languageCode.isBlank() || languageCode == "system") {
                    localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
                } else {
                    localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
                }
                return
            }
        }

        // Fallback for API < 33: update configuration
        val locale = if (languageCode.isBlank() || languageCode == "system") {
            Locale.getDefault()
        } else {
            Locale.forLanguageTag(languageCode)
        }
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun wrapContext(context: Context, languageCode: String): Context {
        if (languageCode.isBlank() || languageCode == "system") {
            return context
        }
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}

data class LanguageOption(
    val code: String,
    val labelRes: String,
    val nativeName: String
)
