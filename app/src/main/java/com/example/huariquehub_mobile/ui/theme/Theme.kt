package com.example.huariquehub_mobile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = OrangeDark,
    onPrimary = BrownDark,
    primaryContainer = BrownMedium,
    onPrimaryContainer = OrangeLight,
    secondary = YellowGreen,
    onSecondary = BrownDark,
    background = SurfaceDark,
    surface = SurfaceDark,
    onBackground = SurfaceColor,
    onSurface = SurfaceColor,
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = SurfaceColor,
    primaryContainer = OrangeLight,
    onPrimaryContainer = BrownDark,
    secondary = BrownMedium,
    onSecondary = SurfaceColor,
    secondaryContainer = YellowGreen,
    onSecondaryContainer = BrownDark,
    tertiary = BrownDark,
    background = WarmWhite,
    surface = SurfaceColor,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed
)

@Composable
fun HuariqueHubMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
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