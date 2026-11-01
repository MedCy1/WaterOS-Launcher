package com.medcy1.wateros.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.medcy1.wateros.launcher.data.AppRepository
import com.medcy1.wateros.launcher.data.PreferencesRepository
import com.medcy1.wateros.launcher.model.AppItem
import com.medcy1.wateros.launcher.theme.WaterOSTheme
import com.medcy1.wateros.launcher.ui.HomeScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var appRepo: AppRepository
    private lateinit var prefsRepo: PreferencesRepository

    private var appsList by mutableStateOf<List<AppItem>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appRepo = AppRepository(this)
        prefsRepo = PreferencesRepository(this)

        setContent {
            val selectedTheme by prefsRepo.theme.collectAsState()
            val gridColumns by prefsRepo.gridColumns.collectAsState()
            val showSeconds by prefsRepo.showSeconds.collectAsState()
            val showLabels by prefsRepo.showLabels.collectAsState()
            val favorites by prefsRepo.favorites.collectAsState()

            WaterOSTheme(selectedTheme = selectedTheme) {
                HomeScreen(
                    apps = appsList,
                    currentTheme = selectedTheme,
                    gridColumns = gridColumns,
                    showSeconds = showSeconds,
                    showLabels = showLabels,
                    onThemeSelect = { prefsRepo.setTheme(it) },
                    onGridColumnsChange = { prefsRepo.setGridColumns(it) },
                    onToggleShowSeconds = { prefsRepo.setShowSeconds(it) },
                    onToggleShowLabels = { prefsRepo.setShowLabels(it) },
                    onToggleFavorite = { pkg ->
                        prefsRepo.toggleFavorite(pkg)
                        refreshApps()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshApps()
    }

    private fun refreshApps() {
        lifecycleScope.launch {
            val favs = prefsRepo.favorites.value
            appsList = appRepo.getInstalledApps(favs)
        }
    }
}
