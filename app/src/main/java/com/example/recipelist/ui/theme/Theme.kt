package com.example.recipelist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = RedGray80,
    secondary = RedGray70,
    tertiary = MainRed,


    background = Color(0xFF1C1B1F),

    surface = Color(0xFF2A282A),

    onPrimary = Color(0xFF332D2E),
    onSecondary = Color(0xFF332D2E),
    onTertiary = Color.White,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

private val LightColorScheme = lightColorScheme(
    primary = RedGray50,
    secondary = RedGray30,
    tertiary = DarkMainRed,

    background = Color(0xFFFFFBFF),
    surface = Color(0xFFFFFBFF),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun RecipeListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}