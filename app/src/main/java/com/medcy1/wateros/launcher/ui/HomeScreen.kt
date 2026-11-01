package com.medcy1.wateros.launcher.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medcy1.wateros.launcher.model.AppCategory
import com.medcy1.wateros.launcher.model.AppItem
import com.medcy1.wateros.launcher.model.ThemeOption
import com.medcy1.wateros.launcher.theme.WaterOSTheme

@Composable
fun HomeScreen(
    apps: List<AppItem>,
    currentTheme: ThemeOption,
    gridColumns: Int,
    showSeconds: Boolean,
    showLabels: Boolean,
    onThemeSelect: (ThemeOption) -> Unit,
    onGridColumnsChange: (Int) -> Unit,
    onToggleShowSeconds: (Boolean) -> Unit,
    onToggleShowLabels: (Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(AppCategory.ALL) }
    var showSettingsModal by remember { mutableStateOf(false) }

    val filteredApps = remember(apps, selectedCategory) {
        when (selectedCategory) {
            AppCategory.ALL -> apps
            AppCategory.FAVORITES -> apps.filter { it.isFavorite }
            AppCategory.MEDIA -> apps.filter { it.category == AppCategory.MEDIA }
            AppCategory.TOOLS -> apps.filter { it.category == AppCategory.TOOLS }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WaterOSTheme.colors.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Top Status & Branding Bar
            TopBar(
                showSeconds = showSeconds,
                onOpenSettings = { showSettingsModal = true }
            )

            // 2. Category Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val categories = listOf(
                    AppCategory.ALL to "Toutes les applications",
                    AppCategory.FAVORITES to "Favoris",
                    AppCategory.MEDIA to "Streaming & Médias",
                    AppCategory.TOOLS to "Outils & Système"
                )

                categories.forEach { (cat, label) ->
                    val isSelected = cat == selectedCategory
                    var isFocused by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) WaterOSTheme.colors.cardFocused else Color.Transparent)
                            .border(
                                width = if (isFocused) 2.dp else if (isSelected) 1.5.dp else 1.dp,
                                color = if (isFocused) WaterOSTheme.colors.focusGlow else if (isSelected) WaterOSTheme.colors.accent else WaterOSTheme.colors.cardFocused,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .onFocusChanged { isFocused = it.isFocused }
                            .focusable()
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected || isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Grid of Apps
            if (filteredApps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Aucune application dans cette section",
                            color = WaterOSTheme.colors.textSecondary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Maintenez le bouton de sélection sur une application pour l'ajouter aux favoris",
                            color = WaterOSTheme.colors.textMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredApps, key = { it.packageName }) { app ->
                        AppCard(
                            app = app,
                            showLabel = showLabels,
                            onLaunch = {
                                if (app.launchIntent != null) {
                                    try {
                                        app.launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(app.launchIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Impossible de lancer : ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Activité introuvable pour ${app.label}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onToggleFavorite = {
                                onToggleFavorite(app.packageName)
                                val msg = if (app.isFavorite) "Retiré des favoris" else "Ajouté aux favoris"
                                Toast.makeText(context, "${app.label} : $msg", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        // Settings Dialog Modal
        if (showSettingsModal) {
            SettingsDialog(
                currentTheme = currentTheme,
                gridColumns = gridColumns,
                showSeconds = showSeconds,
                showLabels = showLabels,
                onThemeSelect = onThemeSelect,
                onGridColumnsChange = onGridColumnsChange,
                onToggleShowSeconds = onToggleShowSeconds,
                onToggleShowLabels = onToggleShowLabels,
                onDismiss = { showSettingsModal = false }
            )
        }
    }
}
