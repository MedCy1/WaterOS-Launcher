package com.medcy1.wateros.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.medcy1.wateros.launcher.model.ThemeOption
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
                .width(540.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(WaterOSTheme.colors.background)
                .border(1.5.dp, WaterOSTheme.colors.cardFocused, RoundedCornerShape(24.dp))
                .padding(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header
                Text(
                    text = "PERSONNALISATION WATEROS",
                    color = WaterOSTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Ajustez le style visuel et le comportement de votre écran d'accueil",
                    color = WaterOSTheme.colors.textSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Section 1: Themes
                Text(
                    text = "THÈME D'INTERFACE",
                    color = WaterOSTheme.colors.accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ThemeOption.values().forEach { theme ->
                        val isSelected = theme == currentTheme
                        var isFocused by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) WaterOSTheme.colors.cardFocused else WaterOSTheme.colors.card)
                                .border(
                                    width = if (isFocused) 2.dp else if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isFocused) WaterOSTheme.colors.focusGlow else if (isSelected) WaterOSTheme.colors.accent else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .clickable { onThemeSelect(theme) }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = theme.displayName,
                                color = if (isSelected || isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 2: Grid Density
                Text(
                    text = "DISPOSITION DE LA GRILLE",
                    color = WaterOSTheme.colors.accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(4, 5, 6).forEach { cols ->
                        val isSelected = cols == gridColumns
                        var isFocused by remember { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) WaterOSTheme.colors.cardFocused else WaterOSTheme.colors.card)
                                .border(
                                    width = if (isFocused) 2.dp else if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isFocused) WaterOSTheme.colors.focusGlow else if (isSelected) WaterOSTheme.colors.accent else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .onFocusChanged { isFocused = it.isFocused }
                                .focusable()
                                .clickable { onGridColumnsChange(cols) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cols Colonnes",
                                color = if (isSelected || isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Section 3: Toggles (Seconds & Labels)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Clock Seconds Toggle
                    var isSecFocused by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (showSeconds) WaterOSTheme.colors.cardFocused else WaterOSTheme.colors.card)
                            .border(
                                width = if (isSecFocused) 2.dp else 1.dp,
                                color = if (isSecFocused) WaterOSTheme.colors.focusGlow else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .onFocusChanged { isSecFocused = it.isFocused }
                            .focusable()
                            .clickable { onToggleShowSeconds(!showSeconds) }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showSeconds) "Secondes : ON" else "Secondes : OFF",
                            color = WaterOSTheme.colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Labels Toggle
                    var isLabFocused by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (showLabels) WaterOSTheme.colors.cardFocused else WaterOSTheme.colors.card)
                            .border(
                                width = if (isLabFocused) 2.dp else 1.dp,
                                color = if (isLabFocused) WaterOSTheme.colors.focusGlow else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .onFocusChanged { isLabFocused = it.isFocused }
                            .focusable()
                            .clickable { onToggleShowLabels(!showLabels) }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showLabels) "Titres : Visibles" else "Titres : Masqués",
                            color = WaterOSTheme.colors.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Close Button
                var isCloseFocused by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isCloseFocused) WaterOSTheme.colors.accent else WaterOSTheme.colors.card)
                        .border(
                            width = 1.dp,
                            color = if (isCloseFocused) Color.Transparent else WaterOSTheme.colors.cardFocused,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .onFocusChanged { isCloseFocused = it.isFocused }
                        .focusable()
                        .clickable { onDismiss() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Valider et fermer",
                        color = if (isCloseFocused) Color.Black else WaterOSTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
