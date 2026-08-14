package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = WaslPrimaryCharcoal,
    onPrimary = Color.White,
    primaryContainer = WaslPrimaryContainer,
    onPrimaryContainer = WaslOnPrimaryContainer,
    secondary = WaslSandGold,
    onSecondary = Color.White,
    secondaryContainer = WaslGoldContainer,
    onSecondaryContainer = WaslPrimaryCharcoal,
    tertiary = WaslSaudiGreen,
    onTertiary = Color.White,
    tertiaryContainer = WaslSaudiGreenLight,
    onTertiaryContainer = WaslSaudiGreen,
    background = WaslBgCream,
    onBackground = WaslTextPrimary,
    surface = WaslSurfaceWhite,
    onSurface = WaslTextPrimary,
    surfaceVariant = WaslSurfaceBeige,
    onSurfaceVariant = WaslTextSecondary,
    outline = WaslBorderBeige,
    outlineVariant = WaslBorderDark
)

private val DarkColorScheme = darkColorScheme(
    primary = WaslGoldLight,
    onPrimary = WaslPrimaryCharcoal,
    primaryContainer = Color(0xFF38312B),
    onPrimaryContainer = WaslGoldLight,
    secondary = WaslSandGold,
    onSecondary = Color.Black,
    tertiary = WaslWhatsAppGreen,
    onTertiary = Color.Black,
    background = Color(0xFF161412),
    onBackground = Color(0xFFF5EFEB),
    surface = Color(0xFF201D1A),
    onSurface = Color(0xFFF5EFEB),
    surfaceVariant = Color(0xFF2C2723),
    onSurfaceVariant = Color(0xFFC7BEB6),
    outline = Color(0xFF453E38),
    outlineVariant = Color(0xFF5A524A)
)

@Composable
fun WaslTheme(
    darkTheme: Boolean = false, // Keep clean white/beige as default branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

