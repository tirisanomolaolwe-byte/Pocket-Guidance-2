package com.example.pocketguidance.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary          = Green40,
    onPrimary        = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Green80,
    secondary        = Teal40,
    error            = Red40,
    background       = Surface,
    surface          = Surface,
    onBackground     = OnSurface,
    onSurface        = OnSurface
)

private val DarkColors = darkColorScheme(
    primary          = Green80,
    onPrimary        = GreenDark,
    primaryContainer = GreenDark,
    secondary        = Teal80,
    error            = Red40,
    background       = SurfaceDark,
    surface          = SurfaceDark,
    onBackground     = OnSurfaceDark,
    onSurface        = OnSurfaceDark
)

@Composable
fun PocketGuidanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
