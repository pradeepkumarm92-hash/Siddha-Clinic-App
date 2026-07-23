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

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkSiddhaGreenPrimary,
    secondary = DarkSiddhaGreenSecondary,
    tertiary = SiddhaGoldAccent,
    background = DarkSiddhaBg,
    surface = DarkSiddhaSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFE1E3DF),
    onSurface = Color(0xFFE1E3DF)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SiddhaGreenPrimary,
    secondary = SiddhaGreenSecondary,
    tertiary = SiddhaGoldAccent,
    background = SiddhaCreamBg,
    surface = SiddhaCardBg,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = SiddhaTextPrimary,
    onSurface = SiddhaTextPrimary,
    surfaceVariant = Color(0xFFE1E8E3),
    onSurfaceVariant = SiddhaTextSecondary
  )

@Composable
fun SiddhaClinicTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Set false to maintain Siddha clinic brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
