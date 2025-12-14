package com.aprouxdev.arcencielplanning.domain.enums

import kotlinx.serialization.Serializable

@Serializable
enum class Teams {
    Clothes,
    Toys,
    Shop,
    Braderie,
    Cleaning,
    Other;

    fun getName(): String {
        return when (this) {
            Shop -> "Magasin"
            Clothes -> "Vêtements"
            Toys -> "Jouets"
            Braderie -> "Braderie"
            Cleaning -> "Ménage"
            Other -> "Autre"
        }
    }

    /**
     * Returns the color in hex format (ARGB)
     */
    fun getColorHex(): Long {
        return when (this) {
            Clothes -> 0xFFFFF5D7  // team_sorting - Light Yellow
            Toys -> 0xFFCCCCFF     // team_toy - Light Purple
            Shop -> 0xFFE6FFCA     // team_shop - Light Green
            Braderie -> 0xFFFFD1DC // team_braderie - Light Pink
            Cleaning -> 0xFFFFCCCC // team_cleaning - Light Red
            Other -> 0xFFCCFFFF    // team_other - Light Cyan
        }
    }

    companion object {
        fun fromName(name: String): Teams {
            return when (name) {
                "Magasin" -> Shop
                "Vêtements" -> Clothes
                "Jouets" -> Toys
                "Braderie" -> Braderie
                "Ménage" -> Cleaning
                "Autre" -> Other
                else -> Other
            }
        }
    }
}

/**
 * Extension function to convert String to Teams enum
 */
fun String.getTeamByName(): Teams = Teams.fromName(this)
