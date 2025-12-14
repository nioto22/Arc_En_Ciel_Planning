package com.aprouxdev.arcencielplanning.ui.theme

import androidx.compose.ui.graphics.Color
import com.aprouxdev.arcencielplanning.domain.enums.Teams

/**
 * Color palette for Arc En Ciel Planning app
 */

// Primary colors
val Primary = Color(0xFF6200EE)
val PrimaryVariant = Color(0xFF3700B3)
val Secondary = Color(0xFF03DAC6)
val SecondaryVariant = Color(0xFF018786)

// Background colors
val Background = Color(0xFFFAFAFA)
val Surface = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFF5F5F5)

// Text colors
val OnPrimary = Color(0xFFFFFFFF)
val OnSecondary = Color(0xFF000000)
val OnBackground = Color(0xFF1C1C1C)
val OnSurface = Color(0xFF1C1C1C)

// Error colors
val Error = Color(0xFFB00020)
val OnError = Color(0xFFFFFFFF)

// Team colors - Background (light tints)
val ClothesBackground = Color(0xFFFFF5D7)
val ToysBackground = Color(0xFFCCCCFF)
val ShopBackground = Color(0xFFE6FFCA)
val BraderieBackground = Color(0xFFFFE5CC)
val CleaningBackground = Color(0xFFD9F2E6)
val OtherBackground = Color(0xFFE0E0E0)

// Team colors - Primary (more saturated for borders/icons)
val ClothesPrimary = Color(0xFFFFD700)
val ToysPrimary = Color(0xFF9999FF)
val ShopPrimary = Color(0xFFB3FF66)
val BraderiePrimary = Color(0xFFFFCC99)
val CleaningPrimary = Color(0xFF80D4AC)
val OtherPrimary = Color(0xFFBDBDBD)

/**
 * Extension function to get background color for a team
 */
fun Teams.getBackgroundColor(): Color {
    return when (this) {
        Teams.Clothes -> ClothesBackground
        Teams.Toys -> ToysBackground
        Teams.Shop -> ShopBackground
        Teams.Braderie -> BraderieBackground
        Teams.Cleaning -> CleaningBackground
        Teams.Other -> OtherBackground
    }
}

/**
 * Extension function to get primary color for a team
 */
fun Teams.getPrimaryColor(): Color {
    return when (this) {
        Teams.Clothes -> ClothesPrimary
        Teams.Toys -> ToysPrimary
        Teams.Shop -> ShopPrimary
        Teams.Braderie -> BraderiePrimary
        Teams.Cleaning -> CleaningPrimary
        Teams.Other -> OtherPrimary
    }
}
