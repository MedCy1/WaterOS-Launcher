package com.medcy1.wateros.launcher.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.medcy1.wateros.launcher.model.AppItem
import com.medcy1.wateros.launcher.theme.WaterOSTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppCard(
    app: AppItem,
    showLabel: Boolean,
    onLaunch: () -> Unit,
    onToggleFavorite: () -> Unit,
    onFocusStateChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    // GSAP-style snappy spring physics
    val cardScale by animateFloatAsState(
        targetValue = if (isFocused) 1.10f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 280f),
        label = "card_spring_scale"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isFocused) 1.06f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
        label = "icon_spring_scale"
    )

    val context = LocalContext.current
    val glowColor = WaterOSTheme.colors.focusGlow

    Box(
        modifier = modifier
            .scale(cardScale)
            .drawBehind {
                if (isFocused) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.38f),
                                glowColor.copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            radius = size.maxDimension * 0.72f
                        )
                    )
                }
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isFocused) {
                    Brush.verticalGradient(
                        listOf(
                            WaterOSTheme.colors.cardFocused,
                            WaterOSTheme.colors.card
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.05f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    )
                }
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                brush = if (isFocused) WaterOSTheme.colors.cardBorderFocused else WaterOSTheme.colors.cardBorderUnfocused,
                shape = RoundedCornerShape(20.dp)
            )
            .onFocusChanged {
                isFocused = it.isFocused
                onFocusStateChange(it.isFocused)
            }
            .focusable()
            .onKeyEvent { event ->
                if (event.type == androidx.compose.ui.input.key.KeyEventType.KeyUp &&
                    (event.key == androidx.compose.ui.input.key.Key.DirectionCenter ||
                     event.key == androidx.compose.ui.input.key.Key.Enter ||
                     event.key == androidx.compose.ui.input.key.Key.NumPadEnter)
                ) {
                    onLaunch()
                    true
                } else if (event.type == androidx.compose.ui.input.key.KeyEventType.KeyDown &&
                    (event.key == androidx.compose.ui.input.key.Key.Menu ||
                     event.key == androidx.compose.ui.input.key.Key.MediaPlayPause)
                ) {
                    onToggleFavorite()
                    true
                } else {
                    false
                }
            }
            .combinedClickable(
                onClick = onLaunch,
                onLongClick = onToggleFavorite
            )
            .padding(vertical = 18.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App Icon Container with micro-depth
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .scale(iconScale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isFocused) Color.White.copy(alpha = 0.08f) else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(app.banner ?: app.icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = app.label,
                    modifier = Modifier.size(58.dp)
                )
            }

            if (showLabel) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = app.label,
                    color = if (isFocused) WaterOSTheme.colors.textPrimary else WaterOSTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium,
                    letterSpacing = 0.3.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Star Pin Badge for Favorites
        if (app.isFavorite) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(WaterOSTheme.colors.accentSecondary.copy(alpha = 0.9f))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favori",
                    tint = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
