package com.medcy1.wateros.launcher.data

import android.content.Context
import android.content.SharedPreferences
import com.medcy1.wateros.launcher.model.ThemeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("wateros_prefs", Context.MODE_PRIVATE)

    private val _theme = MutableStateFlow(loadTheme())
    val theme: StateFlow<ThemeOption> = _theme.asStateFlow()

    private val _favorites = MutableStateFlow(loadFavorites())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _gridColumns = MutableStateFlow(prefs.getInt(KEY_GRID_COLUMNS, 5))
    val gridColumns: StateFlow<Int> = _gridColumns.asStateFlow()

    private val _showSeconds = MutableStateFlow(prefs.getBoolean(KEY_SHOW_SECONDS, false))
    val showSeconds: StateFlow<Boolean> = _showSeconds.asStateFlow()

    private val _showLabels = MutableStateFlow(prefs.getBoolean(KEY_SHOW_LABELS, true))
    val showLabels: StateFlow<Boolean> = _showLabels.asStateFlow()

    fun setTheme(theme: ThemeOption) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
        _theme.value = theme
    }

    fun toggleFavorite(packageName: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply()
        _favorites.value = current
    }

    fun setGridColumns(columns: Int) {
        val sanitized = columns.coerceIn(3, 7)
        prefs.edit().putInt(KEY_GRID_COLUMNS, sanitized).apply()
        _gridColumns.value = sanitized
    }

    fun setShowSeconds(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_SECONDS, show).apply()
        _showSeconds.value = show
    }

    fun setShowLabels(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_LABELS, show).apply()
        _showLabels.value = show
    }

    private fun loadTheme(): ThemeOption {
        val raw = prefs.getString(KEY_THEME, ThemeOption.WATER_NEON.name)
        return try {
            ThemeOption.valueOf(raw ?: ThemeOption.WATER_NEON.name)
        } catch (_: Exception) {
            ThemeOption.WATER_NEON
        }
    }

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    companion object {
        private const val KEY_THEME = "key_theme"
        private const val KEY_FAVORITES = "key_favorites"
        private const val KEY_GRID_COLUMNS = "key_grid_columns"
        private const val KEY_SHOW_SECONDS = "key_show_seconds"
        private const val KEY_SHOW_LABELS = "key_show_labels"
    }
}
