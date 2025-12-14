package com.aprouxdev.arcencielplanning

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.aprouxdev.arcencielplanning.ui.screens.setup.SetupScreen
import com.aprouxdev.arcencielplanning.ui.theme.ArcEnCielTheme

/**
 * Main entry point for the Compose Multiplatform app
 */
@Composable
fun App() {
    ArcEnCielTheme {
        Navigator(SetupScreen())
    }
}