package com.aprouxdev.arcencielplanning.data.enums

import android.graphics.Color
import com.aprouxdev.arcencielplanning.R


enum class Teams {
    Clothes, Toys, Shop, Braderie, Cleaning, Other;

    fun getName(): String {
        return when(this) {
            Shop -> "Magasin"
            Clothes -> "Vêtements"
            Toys -> "Jouets"
            Braderie -> "Braderie"
            Cleaning -> "Ménage"
            Other -> "Autre"
        }
    }



    fun getColorRes(): Int {
        return when(this) {
            Clothes -> R.color.team_sorting
            Toys -> R.color.team_toy
            Shop -> R.color.team_shop
            Braderie -> R.color.team_braderie
            Cleaning -> R.color.team_cleaning
            Other -> R.color.team_other
        }
    }
}

fun String.getTeamByName(): Teams {
    return when(this) {
        "Magasin" -> Teams.Shop
        "Vêtements" -> Teams.Clothes
        "Jouets" -> Teams.Toys
        "Braderie" -> Teams.Braderie
        "Ménage" -> Teams.Cleaning
        "Autre" -> Teams.Other
        else -> Teams.Other
    }
}