package com.medcy1.wateros.launcher.model

import android.content.Intent
import android.graphics.drawable.Drawable

enum class AppCategory {
    ALL,
    FAVORITES,
    MEDIA,
    TOOLS
}

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val banner: Drawable? = null,
    val launchIntent: Intent? = null,
    val isFavorite: Boolean = false,
    val category: AppCategory = AppCategory.ALL
)
