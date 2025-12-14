package com.aprouxdev.arcencielplanning.domain.enums

import kotlinx.serialization.Serializable

@Serializable
enum class AlertType {
    General,
    Team,
    Shop,
    Urgency,
    Assiduity;

    fun getDisplayName(): String {
        return when (this) {
            General -> "Informations générales"
            Team -> "Informations équipe"
            Shop -> "Informations magasin"
            Urgency -> "Informations urgentes"
            Assiduity -> "Rappel assiduité"
        }
    }

    /**
     * Returns the color in hex format (ARGB)
     */
    fun getColorHex(): Long {
        return when (this) {
            General -> 0xFFFF9800  // Orange
            Team -> 0xFF92CCE8    // Blue
            Shop -> 0xFFAFFFAF    // Green
            Urgency -> 0xFFE5707E // Red
            Assiduity -> 0xFFFFE9AE // Yellow
        }
    }

    companion object {
        fun fromName(name: String): AlertType {
            return when (name.lowercase()) {
                "general", "generale" -> General
                "team", "équipe", "equipe" -> Team
                "shop", "magasin" -> Shop
                "urgency", "urgente", "urgent" -> Urgency
                "assiduity", "assiduité", "assiduite" -> Assiduity
                else -> General
            }
        }
    }
}

/**
 * Extension function to convert String to AlertType enum
 */
fun String.getAlertTypeByName(): AlertType = AlertType.fromName(this)
