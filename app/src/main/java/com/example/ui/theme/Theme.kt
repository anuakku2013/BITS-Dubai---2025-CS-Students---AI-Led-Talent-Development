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

private val DarkColorScheme = darkColorScheme(
    primary = GeoProgressIndicator,
    onPrimary = Color.White,
    secondary = GeoPrimaryDark,
    onSecondary = Color.White,
    tertiary = NeonPurple,
    onTertiary = Color.White,
    background = GeoBackground,
    onBackground = GeoTextPrimary,
    surface = GeoSurface,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoSurfaceVariant,
    onSurfaceVariant = GeoTextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = GeoProgressIndicator,
    onPrimary = Color.White,
    secondary = GeoPrimaryDark,
    onSecondary = Color.White,
    tertiary = NeonPurple,
    onTertiary = Color.White,
    background = GeoBackground,
    onBackground = GeoTextPrimary,
    surface = GeoSurface,
    onSurface = GeoTextPrimary,
    surfaceVariant = GeoSurfaceVariant,
    onSurfaceVariant = GeoTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme // Force our gorgeous, high-contrast, light "Geometric Balance" design theme!


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
