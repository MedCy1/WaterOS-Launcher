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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
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

    // Dynamic Hero Spotlight tracking
    var focusedApp by remember { mutableStateOf<AppItem?>(null) }

    val filteredApps = remember(apps, selectedCategory) {
        when (selectedCategory) {
            AppCategory.ALL -> apps
            AppCategory.FAVORITES -> apps.filter { it.isFavorite }
            AppCategory.MEDIA -> apps.filter { it.category == AppCategory.MEDIA }
            AppCategory.TOOLS -> apps.filter { it.category == AppCategory.TOOLS }
        }
    }

    // Default focused app to first item if null
    LaunchedEffect(filteredApps) {
        if (focusedApp == null && filteredApps.isNotEmpty()) {
            focusedApp = filteredApps.firstOrNull()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WaterOSTheme.colors.backgroundBrush)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Top Status & Telemetry Bar
            TopBar(
                showSeconds = showSeconds,
                onOpenSettings = { showSettingsModal = true }
            )

            // 2. Cinematic Hero Spotlight Section
            HeroSpotlight(
                focusedApp = focusedApp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Segmented Glass Navigation Capsule & Quick Settings Trigger
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Selector
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.04f),
                                    Color.White.copy(alpha = 0.02f)
                                )
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(16.dp))
                        .padding(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isFocused -> WaterOSTheme.colors.cardFocused
                                            isSelected -> WaterOSTheme.colors.accent.copy(alpha = 0.16f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = if (isFocused) 1.5.dp else if (isSelected) 1.dp else 0.dp,
                                        color = when {
                                            isFocused -> WaterOSTheme.colors.focusGlow
                                            isSelected -> WaterOSTheme.colors.accent.copy(alpha = 0.45f)
                                            else -> Color.Transparent
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .onFocusChanged { isFocused = it.isFocused }
                                    .focusable()
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = when {
                                        isFocused -> WaterOSTheme.colors.accent
                                        isSelected -> WaterOSTheme.colors.textPrimary
                                        else -> WaterOSTheme.colors.textSecondary
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Medium,
                                    letterSpacing = 0.4.sp
                                )
                            }
                        }
                    }
                }

                // Quick Settings Action Capsule
                var isSettingsFocused by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSettingsFocused) WaterOSTheme.colors.cardFocused else Color.White.copy(alpha = 0.04f)
                        )
                        .border(
                            width = if (isSettingsFocused) 1.5.dp else 1.dp,
                            color = if (isSettingsFocused) WaterOSTheme.colors.focusGlow else Color.White.copy(alpha = 0.07f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .onFocusChanged { isSettingsFocused = it.isFocused }
                        .focusable()
                        .clickable { showSettingsModal = true }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = "PARAMÈTRES",
                        color = if (isSettingsFocused) WaterOSTheme.colors.accent else WaterOSTheme.colors.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Interactive Grid of Apps with GSAP-grade Spring Physics
            if (filteredApps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 48.dp, vertical = 32.dp)
                    ) {
                        Text(
                            text = "Aucune application dans cette section",
                            color = WaterOSTheme.colors.textPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Maintenez le bouton de validation sur une application pour l'ajouter aux favoris",
                            color = WaterOSTheme.colors.textSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    contentPadding = PaddingValues(start = 36.dp, end = 36.dp, top = 8.dp, bottom = 28.dp),
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
                                        Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Activité introuvable pour ${app.label}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onToggleFavorite = {
                                onToggleFavorite(app.packageName)
                                val msg = if (app.isFavorite) "Retiré des favoris" else "Ajouté aux favoris"
                                Toast.makeText(context, "${app.label} : $msg", Toast.LENGTH_SHORT).show()
                            },
                            onFocusStateChange = { focused ->
                                if (focused) {
                                    focusedApp = app
                                }
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
