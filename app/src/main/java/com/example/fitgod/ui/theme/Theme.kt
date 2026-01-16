
package com.example.fitgod.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Enforce Light Theme for "Workouts" look
private val CleanLightColorScheme = lightColorScheme(
    primary = PrimaryBrand,
    onPrimary = CleanWhite,
    secondary = PrimaryBrand,
    onSecondary = CleanWhite,
    tertiary = TextGrey,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = CleanGrey,
    onSurfaceVariant = TextGrey,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun FitGodTheme(
    darkTheme: Boolean = false, // Force Light Theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = CleanLightColorScheme
    
    // Set Status Bar Color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // White status bar
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true // Dark icons
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}