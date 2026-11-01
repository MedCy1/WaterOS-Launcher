package com.medcy1.wateros.launcher.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.medcy1.wateros.launcher.model.ThemeOption

@Immutable
data class WaterOSColors(
    val background: Color,
    val card: Color,
    val cardFocused: Color,
    val accent: Color,
    val accentSecondary: Color,
    val focusGlow: Color,
    val textPrimary: Color = TextWhite,
    val textSecondary: Color = TextSub,
    val textMuted: Color = TextMuted
)

val LocalWaterOSColors = staticCompositionLocalOf {
    WaterOSColors(
        background = WaterNeonBackground,
        card = WaterNeonCard,
        cardFocused = WaterNeonCardFocused,
        accent = WaterNeonAccent,
        accentSecondary = WaterNeonAccentSecondary,
        focusGlow = WaterNeonFocusGlow
    )
}

fun getColorsForTheme(theme: ThemeOption): WaterOSColors {
    return when (theme) {
        ThemeOption.WATER_NEON -> WaterOSColors(
            background = WaterNeonBackground,
            card = WaterNeonCard,
            cardFocused = WaterNeonCardFocused,
            accent = WaterNeonAccent,
            accentSecondary = WaterNeonAccentSecondary,
            focusGlow = WaterNeonFocusGlow
        )
        ThemeOption.OLED_BLACK -> WaterOSColors(
            background = OledBackground,
            card = OledCard,
            cardFocused = OledCardFocused,
            accent = OledAccent,
            accentSecondary = OledAccentSecondary,
            focusGlow = OledFocusGlow
        )
        ThemeOption.NORDIC_FROST -> WaterOSColors(
            background = NordicBackground,
            card = NordicCard,
            cardFocused = NordicCardFocused,
            accent = NordicAccent,
            accentSecondary = NordicAccentSecondary,
            focusGlow = NordicFocusGlow
        )
        ThemeOption.CYBER_AMBER -> WaterOSColors(
            background = CyberBackground,
            card = CyberCard,
            cardFocused = CyberCardFocused,
            accent = CyberAccent,
            accentSecondary = CyberAccentSecondary,
            focusGlow = CyberFocusGlow
        )
    }
}

@Composable
fun WaterOSTheme(
    selectedTheme: ThemeOption = ThemeOption.WATER_NEON,
    content: @Composable () -> Unit
) {
    val colors = getColorsForTheme(selectedTheme)

    CompositionLocalProvider(
        LocalWaterOSColors provides colors,
        content = content
    )
}

object WaterOSTheme {
    val colors: WaterOSColors
        @Composable
        get() = LocalWaterOSColors.current
}
