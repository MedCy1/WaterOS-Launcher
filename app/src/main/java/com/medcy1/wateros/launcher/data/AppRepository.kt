package com.medcy1.wateros.launcher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.medcy1.wateros.launcher.model.AppCategory
import com.medcy1.wateros.launcher.model.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager
    private val selfPackage: String = context.packageName

    suspend fun getInstalledApps(favorites: Set<String>): List<AppItem> = withContext(Dispatchers.IO) {
        val appList = mutableListOf<AppItem>()
        val seenPackages = mutableSetOf<String>()

        // 1. Query Leanback Launcher activities (TV optimized)
        val leanbackIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        val leanbackResolves = packageManager.queryIntentActivities(leanbackIntent, 0)

        // 2. Query Standard Launcher activities (fallback)
        val standardIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val standardResolves = packageManager.queryIntentActivities(standardIntent, 0)

        val combinedResolves = mutableListOf<ResolveInfo>()
        combinedResolves.addAll(leanbackResolves)
        combinedResolves.addAll(standardResolves)

        for (resolve in combinedResolves) {
            val pkg = resolve.activityInfo.packageName
            if (pkg == selfPackage || seenPackages.contains(pkg)) {
                continue
            }
            seenPackages.add(pkg)

            val label = resolve.loadLabel(packageManager).toString()
            val icon = resolve.loadIcon(packageManager)
            val banner = resolve.activityInfo.loadBanner(packageManager)
            val launchIntent = packageManager.getLaunchIntentForPackage(pkg)

            val category = categorizeApp(pkg, label)
            val isFav = favorites.contains(pkg)

            appList.add(
                AppItem(
                    packageName = pkg,
                    label = label,
                    icon = icon,
                    banner = banner,
                    launchIntent = launchIntent,
                    isFavorite = isFav,
                    category = category
                )
            )
        }

        // Sort alphabetically, with favorites prioritized
        appList.sortedWith(
            compareByDescending<AppItem> { it.isFavorite }
                .thenBy { it.label.lowercase() }
        )
    }

    private fun categorizeApp(pkg: String, label: String): AppCategory {
        val lowerPkg = pkg.lowercase()
        val lowerLabel = label.lowercase()

        val mediaKeywords = listOf(
            "youtube", "smarttube", "netflix", "prime", "video", "disney", "kodi",
            "plex", "jellyfin", "stremio", "twitch", "spotify", "vlc", "hbo",
            "max", "cinema", "player", "music", "audio", "tv"
        )
        for (kw in mediaKeywords) {
            if (lowerPkg.contains(kw) || lowerLabel.contains(kw)) {
                return AppCategory.MEDIA
            }
        }

        val toolsKeywords = listOf(
            "settings", "waterstick", "terminal", "file", "explorer", "installer",
            "adb", "dns", "speedtest", "wifi", "network", "tool", "cleaner", "sys"
        )
        for (kw in toolsKeywords) {
            if (lowerPkg.contains(kw) || lowerLabel.contains(kw)) {
                return AppCategory.TOOLS
            }
        }

        return AppCategory.ALL
    }
}
