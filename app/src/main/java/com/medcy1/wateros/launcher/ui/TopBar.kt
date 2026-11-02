package com.medcy1.wateros.launcher.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medcy1.wateros.launcher.theme.WaterOSTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopBar(
    showSeconds: Boolean,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }

    LaunchedEffect(showSeconds) {
        val timePattern = if (showSeconds) "HH:mm:ss" else "HH:mm"
        val timeFormat = SimpleDateFormat(timePattern, Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE d MMMM", Locale.FRENCH)

        while (true) {
            val now = Date()
            currentTime = timeFormat.format(now)
            currentDate = dateFormat.format(now).replaceFirstChar { it.uppercase() }
            delay(1000)
        }
    }

    // Glowing dot pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: WaterOS Identity & Hardware Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Branding Capsule
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                WaterOSTheme.colors.cardFocused.copy(alpha = 0.65f),
                                WaterOSTheme.colors.card.copy(alpha = 0.5f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        WaterOSTheme.colors.accent.copy(alpha = 0.35f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(WaterOSTheme.colors.accent.copy(alpha = dotAlpha))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "WATEROS",
                        color = WaterOSTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "4K MAX",
                        color = WaterOSTheme.colors.accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Subsystem Telemetry Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Bouclier",
                        tint = WaterOSTheme.colors.accentSecondary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BOUCLIER OTA ACTIF",
                        color = WaterOSTheme.colors.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Right: Date, Live Clock & Settings
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Date & Network status
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currentDate,
                    color = WaterOSTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Wi-Fi",
                        tint = WaterOSTheme.colors.accent,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Wi-Fi 6  •  DoT AdGuard",
                        color = WaterOSTheme.colors.textMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Big Modern Clock
            Text(
                text = currentTime,
                color = WaterOSTheme.colors.textPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )

            // Settings Trigger Button
            var isSettingsFocused by remember { mutableStateOf(false) }
            val settingsScale by animateFloatAsState(
                targetValue = if (isSettingsFocused) 1.15f else 1.0f,
                animationSpec = spring(dampingRatio = 0.65f, stiffness = 300f),
                label = "settings_scale"
            )
            val settingsRotation by animateFloatAsState(
                targetValue = if (isSettingsFocused) 45f else 0f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = 250f),
                label = "settings_rot"
            )

            Box(
                modifier = Modifier
                    .scale(settingsScale)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSettingsFocused) WaterOSTheme.colors.cardFocused else WaterOSTheme.colors.card
                    )
                    .border(
                        width = if (isSettingsFocused) 2.dp else 1.dp,
                        color = if (isSettingsFocused) WaterOSTheme.colors.focusGlow else Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .onFocusChanged { isSettingsFocused = it.isFocused }
                    .focusable()
                    .clickable { onOpenSettings() }
                    .padding(11.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Paramètres",
                    tint = if (isSettingsFocused) WaterOSTheme.colors.accent else WaterOSTheme.colors.textSecondary,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(settingsRotation)
                )
            }
        }
    }
}
