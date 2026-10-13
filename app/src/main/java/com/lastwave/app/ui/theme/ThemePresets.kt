package com.lastwave.app.ui.theme

import androidx.compose.ui.graphics.Color
import com.lastwave.app.data.local.ThemeMode

/**
 * AKWave curated theme presets, inspired by the brand reference boards.
 * Each preset pairs a seed accent color with a light/dark mode.
 */
data class ThemePreset(
    val id: String,
    val name: String,
    val seedColor: Color,
    val themeMode: ThemeMode,
    val description: String,
)

val AKWaveThemePresets: List<ThemePreset> = listOf(
    ThemePreset(
        id = "crimson_glass",
        name = "Crimson Glass",
        seedColor = Color(0xFFC41E2E),
        themeMode = ThemeMode.DARK,
        description = "Deep red glassmorphism",
    ),
    ThemePreset(
        id = "ocean_mist",
        name = "Ocean Mist",
        seedColor = Color(0xFF5B8DB8),
        themeMode = ThemeMode.DARK,
        description = "Cool blue-grey minimal",
    ),
    ThemePreset(
        id = "golden_hour",
        name = "Golden Hour",
        seedColor = Color(0xFFD4A05A),
        themeMode = ThemeMode.LIGHT,
        description = "Warm beige gold",
    ),
    ThemePreset(
        id = "porcelain",
        name = "Porcelain",
        seedColor = Color(0xFF7A7A8E),
        themeMode = ThemeMode.LIGHT,
        description = "Clean neutral light",
    ),
)
