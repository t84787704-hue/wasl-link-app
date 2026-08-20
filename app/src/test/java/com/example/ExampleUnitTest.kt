package com.example

import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun googleMapsGpsUrl_isCorrectlyFormatted() {
    val lat = 24.71360
    val lng = 46.67530
    val formattedLat = String.format(Locale.US, "%.5f", lat)
    val formattedLng = String.format(Locale.US, "%.5f", lng)
    val url = "https://maps.google.com/?q=$formattedLat,$formattedLng"
    assertEquals("https://maps.google.com/?q=24.71360,46.67530", url)
  }

  @Test
  fun currencySymbols_and_formatting_areCorrect() {
    assertEquals("﷼", com.example.data.CurrencyHelper.getCurrencySymbol("SAR"))
    assertEquals("$", com.example.data.CurrencyHelper.getCurrencySymbol("USD"))
    assertEquals("Rs", com.example.data.CurrencyHelper.getCurrencySymbol("PKR"))
    assertEquals("₹", com.example.data.CurrencyHelper.getCurrencySymbol("INR"))
    assertEquals("¥", com.example.data.CurrencyHelper.getCurrencySymbol("CNY"))
    assertEquals("€", com.example.data.CurrencyHelper.getCurrencySymbol("EUR"))
    assertEquals("£", com.example.data.CurrencyHelper.getCurrencySymbol("GBP"))
    assertEquals("AED", com.example.data.CurrencyHelper.getCurrencySymbol("AED"))
    assertEquals("₺", com.example.data.CurrencyHelper.getCurrencySymbol("TRY"))
    assertEquals("৳", com.example.data.CurrencyHelper.getCurrencySymbol("BDT"))

    assertEquals("$ 18", com.example.data.CurrencyHelper.formatPrice("18", "USD"))
    assertEquals("Rs 250", com.example.data.CurrencyHelper.formatPrice("250", "PKR"))
    assertEquals("₹ 120", com.example.data.CurrencyHelper.formatPrice("120", "INR"))
    assertEquals("﷼ 18", com.example.data.CurrencyHelper.formatPrice("18 SAR", "SAR"))
  }

  @Test
  fun parseMenuItems_formatsPricesWithSelectedCurrency() {
    val rawMenu = "• Latte - 18\n• Cappuccino - 20 SAR"
    val parsedUsd = com.example.ui.screens.parseMenuItems(rawMenu, "USD")
    assertEquals(2, parsedUsd.size)
    assertEquals("Latte", parsedUsd[0].title)
    assertEquals("$ 18", parsedUsd[0].price)
    assertEquals("Cappuccino", parsedUsd[1].title)
    assertEquals("$ 20", parsedUsd[1].price)
  }

  @Test
  fun countryCodeHelper_formatting_and_cleaning_areCorrect() {
    // Saudi Arabia (+966)
    assertEquals("501234567", com.example.data.CountryCodeHelper.cleanLocalNumber("0501234567", "966"))
    assertEquals("501234567", com.example.data.CountryCodeHelper.cleanLocalNumber("966501234567", "966"))
    assertEquals("966501234567", com.example.data.CountryCodeHelper.formatFullInternational("966", "0501234567"))
    assertEquals("+966 501234567", com.example.data.CountryCodeHelper.formatDisplayInternational("966", "501234567"))

    // Pakistan (+92)
    assertEquals("3012345678", com.example.data.CountryCodeHelper.cleanLocalNumber("03012345678", "92"))
    assertEquals("923012345678", com.example.data.CountryCodeHelper.formatFullInternational("92", "03012345678"))
    assertEquals("+92 3012345678", com.example.data.CountryCodeHelper.formatDisplayInternational("92", "3012345678"))

    // India (+91)
    assertEquals("9876543210", com.example.data.CountryCodeHelper.cleanLocalNumber("9876543210", "91"))
    assertEquals("919876543210", com.example.data.CountryCodeHelper.formatFullInternational("91", "9876543210"))
    assertEquals("+91 9876543210", com.example.data.CountryCodeHelper.formatDisplayInternational("91", "9876543210"))

    // China (+86)
    assertEquals("8613812345678", com.example.data.CountryCodeHelper.formatFullInternational("86", "13812345678"))

    // Search tests
    val pakSearch = com.example.data.CountryCodeHelper.searchCountries("pakistan")
    assertTrue(pakSearch.any { it.code == "92" })

    val codeSearch = com.example.data.CountryCodeHelper.searchCountries("+91")
    assertTrue(codeSearch.any { it.code == "91" })
  }
}
