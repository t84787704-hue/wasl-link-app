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
}
