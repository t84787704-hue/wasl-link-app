package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    onPrimary = Color(0xFF1E1A16),
    primaryContainer = Color(0xFF38312B),
    onPrimaryContainer = WaslGoldLight,
    secondary = WaslSandGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF352B1E),
    onSecondaryContainer = WaslGoldLight,
    tertiary = WaslSaudiGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF123B22),
    onTertiaryContainer = Color(0xFF8CEAB1),
    background = Color(0xFF141210),
    onBackground = Color(0xFFF3ECE4),
    surface = Color(0xFF1E1A17),
    onSurface = Color(0xFFF3ECE4),
    surfaceVariant = Color(0xFF2B2621),
    onSurfaceVariant = Color(0xFFC9BFB4),
    outline = Color(0xFF453D34),
    outlineVariant = Color(0xFF5B5145)
)

@Composable
fun WaslTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


