package com.fleetsmart.mockupsfleetsmartmovil.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.PrimaryForeground,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.SecondaryForeground,
    tertiary = AppColors.Accent,
    onTertiary = AppColors.AccentForeground,
    background = AppColors.Background,
    onBackground = AppColors.Foreground,
    surface = AppColors.Card,
    onSurface = AppColors.CardForeground,
    surfaceVariant = AppColors.Muted,
    onSurfaceVariant = AppColors.MutedForeground,
    error = AppColors.Destructive,
    onError = AppColors.DestructiveForeground,
    outline = AppColors.Border,
    outlineVariant = AppColors.Border,
)

@Composable
fun MockUpsFleetSmartMovilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}