package com.medcy1.wateros.launcher.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.medcy1.wateros.launcher.model.ThemeOption
import com.medcy1.wateros.launcher.theme.CyberAccent
import com.medcy1.wateros.launcher.theme.NordicAccent
import com.medcy1.wateros.launcher.theme.OledAccent
import com.medcy1.wateros.launcher.theme.WaterNeonAccent
import com.medcy1.wateros.launcher.theme.WaterOSTheme

@Composable
fun SettingsDialog(
    currentTheme: ThemeOption,
    gridColumns: Int,
    showSeconds: Boolean,
    showLabels: Boolean,
    onThemeSelect: (ThemeOption) -> Unit,
    onGridColumnsChange: (Int) -> Unit,
    onToggleShowSeconds: (Boolean) -> Unit,
    onToggleShowLabels: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(620.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF131A2B),
                            Color(0xFF090D17)
                        )
                    )
                )
                .border(
                    1.5.dp,
                    WaterOSTheme.colors.cardBorderFocused,
                    RoundedCornerShape(28.dp)
                )
                .onKeyEvent { event ->
                    if (event.key == Key.Back && event.type == KeyEventType.KeyUp) {
                        onDismiss()
                        true
                    } else {
                        false
                    }
                }
                .padding(32.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header with glow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PERSONNALISATION WATEROS",
                            color = WaterOSTheme.colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ajustez la direction artistique et le comportement de votre écran d'accueil",
                            color = WaterOSTheme.colors.textSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(WaterOSTheme.colors.accent.copy(alpha = 0.15f))
                            .border(1.dp, WaterOSTheme.colors.accent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "v1.0.0",
                            color = WaterOSTheme.colors.accent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 1: Themes with Color Swatches
                Text(
                    text = "THÈME GRAPHIQUE & AMBIANCE",
                    color = WaterOSTheme.colors.accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ThemeOption.values().forEach { theme ->
                        val isSelected = theme == currentTheme
                        var isFocused by remember { mutableStateOf(false) }

                        val scale by animateFloatAsState(
                            targetValue = if (isFocused) 1.05f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                            label = "theme_scale"
                        )

                        val swatchColor = when (theme) {
                            ThemeOption.WATER_NEON -> WaterNeonAccent
                            ThemeOption.OLED_BLACK -> OledAccent
                            ThemeOption.NORDIC_FROST -> NordicAccent
                            ThemeOption.CYBER_AMBER -> CyberAccent
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .scale(scale)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    when {
                                        isFocused -> WaterOSTheme.colors.cardFocused
                                        isSelected -> Color.White.copy(alpha = 0.08f)
                                        else -> Color.White.copy(alpha = 0.03f)
                                    }
                                )
                                .border(
                                    width = if (isFocused) 2.dp else if (isSelected) 1.5.dp else 1.dp,
                                    color = when {
                                        isFocused -> WaterOSTheme.colors.focusGlow
                                        isSelected -> WaterOSTheme.colors.accent
                                        else -> Color.White.copy(alpha = 0.06f)
                                    },
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .tvClickable { onThemeSelect(theme) }
                                .padding(vertical = 14.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(swatchColor)
                                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = theme.displayName,
                                    color = if (isSelected || isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Section 2: Grid Density
                Text(
                    text = "DENSITÉ DE LA GRILLE",
                    color = WaterOSTheme.colors.accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(4, 5, 6).forEach { cols ->
                        val isSelected = cols == gridColumns
                        var isFocused by remember { mutableStateOf(false) }

                        val scale by animateFloatAsState(
                            targetValue = if (isFocused) 1.05f else 1.0f,
                            animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                            label = "col_scale"
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .scale(scale)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    when {
                                        isFocused -> WaterOSTheme.colors.cardFocused
                                        isSelected -> Color.White.copy(alpha = 0.08f)
                                        else -> Color.White.copy(alpha = 0.03f)
                                    }
                                )
                                .border(
                                    width = if (isFocused) 2.dp else if (isSelected) 1.5.dp else 1.dp,
                                    color = when {
                                        isFocused -> WaterOSTheme.colors.focusGlow
                                        isSelected -> WaterOSTheme.colors.accent
                                        else -> Color.White.copy(alpha = 0.06f)
                                    },
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .tvClickable { onGridColumnsChange(cols) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cols Colonnes",
                                color = if (isSelected || isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Section 3: Switches / Toggles (Seconds & Labels)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Clock Seconds Toggle
                    var isSecFocused by remember { mutableStateOf(false) }
                    val secScale by animateFloatAsState(
                        targetValue = if (isSecFocused) 1.04f else 1.0f,
                        animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                        label = "sec_scale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(secScale)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSecFocused) WaterOSTheme.colors.cardFocused else Color.White.copy(alpha = 0.03f)
                            )
                            .border(
                                width = if (isSecFocused) 2.dp else 1.dp,
                                color = if (isSecFocused) WaterOSTheme.colors.focusGlow else Color.White.copy(alpha = 0.06f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .onFocusChanged { isSecFocused = it.isFocused }
                            .focusable()
                            .tvClickable { onToggleShowSeconds(!showSeconds) }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showSeconds) "Secondes Horloge : ON" else "Secondes Horloge : OFF",
                            color = if (isSecFocused) WaterOSTheme.colors.accent else WaterOSTheme.colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Labels Toggle
                    var isLabFocused by remember { mutableStateOf(false) }
                    val labScale by animateFloatAsState(
                        targetValue = if (isLabFocused) 1.04f else 1.0f,
                        animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                        label = "lab_scale"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .scale(labScale)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isLabFocused) WaterOSTheme.colors.cardFocused else Color.White.copy(alpha = 0.03f)
                            )
                            .border(
                                width = if (isLabFocused) 2.dp else 1.dp,
                                color = if (isLabFocused) WaterOSTheme.colors.focusGlow else Color.White.copy(alpha = 0.06f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .onFocusChanged { isLabFocused = it.isFocused }
                            .focusable()
                            .tvClickable { onToggleShowLabels(!showLabels) }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showLabels) "Titres des Apps : Visibles" else "Titres des Apps : Masqués",
                            color = if (isLabFocused) WaterOSTheme.colors.accent else WaterOSTheme.colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                // Close Button
                var isCloseFocused by remember { mutableStateOf(false) }
                val closeScale by animateFloatAsState(
                    targetValue = if (isCloseFocused) 1.03f else 1.0f,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                    label = "close_scale"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(closeScale)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isCloseFocused) {
                                Brush.horizontalGradient(
                                    listOf(
                                        WaterOSTheme.colors.accent,
                                        WaterOSTheme.colors.accentSecondary
                                    )
                                )
                            } else {
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.08f),
                                        Color.White.copy(alpha = 0.04f)
                                    )
                                )
                            }
                        )
                        .border(
                            width = 1.dp,
                            color = if (isCloseFocused) Color.Transparent else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .onFocusChanged { isCloseFocused = it.isFocused }
                        .focusable()
                        .tvClickable { onDismiss() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "APPLIQUER ET RETOURNER À L'ACCUEIL",
                        color = if (isCloseFocused) Color.Black else WaterOSTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
