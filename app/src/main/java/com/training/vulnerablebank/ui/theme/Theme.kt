package com.training.vulnerablebank.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Gold500,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = Slate100,
    secondary = Mint400,
    onSecondary = Navy900,
    secondaryContainer = Navy700,
    onSecondaryContainer = Slate100,
    background = Navy900,
    onBackground = Slate100,
    surface = Navy700,
    onSurface = Slate100,
    errorContainer = Coral900,
    onErrorContainer = Coral200
)

private val LightColorScheme = lightColorScheme(
    primary = Navy700,
    onPrimary = Slate100,
    primaryContainer = Gold500,
    onPrimaryContainer = Navy900,
    secondary = Mint400,
    onSecondary = Navy900,
    secondaryContainer = Slate100,
    onSecondaryContainer = Navy900,
    background = Slate100,
    onBackground = Navy900,
    surface = Color.White,
    onSurface = Navy900,
    errorContainer = Coral200,
    onErrorContainer = Coral900
)

@Composable
fun VulnerableBankAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
