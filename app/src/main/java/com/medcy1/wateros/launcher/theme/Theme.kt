package com.medcy1.wateros.launcher.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.medcy1.wateros.launcher.model.ThemeOption

@Immutable
data class WaterOSColors(
    val bgStart: Color,
    val bgEnd: Color,
    val card: Color,
    val cardFocused: Color,
    val accent: Color,
    val accentSecondary: Color,
    val focusGlow: Color,
    val textPrimary: Color = TextWhite,
    val textSecondary: Color = TextSub,
    val textMuted: Color = TextMuted
) {
    val backgroundBrush: Brush
        get() = Brush.radialGradient(
            colors = listOf(bgStart, bgEnd),
            radius = 1600f
        )

    val cardBorderUnfocused: Brush
        get() = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.09f), Color.White.copy(alpha = 0.02f))
        )

    val cardBorderFocused: Brush
        get() = Brush.linearGradient(
            colors = listOf(accent, accentSecondary)
        )
}

val LocalWaterOSColors = staticCompositionLocalOf {
    getColorsForTheme(ThemeOption.WATER_NEON)
}

fun getColorsForTheme(theme: ThemeOption): WaterOSColors {
    return when (theme) {
        ThemeOption.WATER_NEON -> WaterOSColors(
            bgStart = WaterNeonBgStart,
            bgEnd = WaterNeonBgEnd,
            card = WaterNeonCard,
            cardFocused = WaterNeonCardFocused,
            accent = WaterNeonAccent,
            accentSecondary = WaterNeonAccentSecondary,
            focusGlow = WaterNeonGlow
        )
        ThemeOption.OLED_BLACK -> WaterOSColors(
            bgStart = OledBgStart,
            bgEnd = OledBgEnd,
            card = OledCard,
            cardFocused = OledCardFocused,
            accent = OledAccent,
            accentSecondary = OledAccentSecondary,
            focusGlow = OledGlow
        )
        ThemeOption.NORDIC_FROST -> WaterOSColors(
            bgStart = NordicBgStart,
            bgEnd = NordicBgEnd,
            card = NordicCard,
            cardFocused = NordicCardFocused,
            accent = NordicAccent,
            accentSecondary = NordicAccentSecondary,
            focusGlow = NordicGlow
        )
        ThemeOption.CYBER_AMBER -> WaterOSColors(
            bgStart = CyberBgStart,
            bgEnd = CyberBgEnd,
            card = CyberCard,
            cardFocused = CyberCardFocused,
            accent = CyberAccent,
            accentSecondary = CyberAccentSecondary,
            focusGlow = CyberGlow
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
