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
}
