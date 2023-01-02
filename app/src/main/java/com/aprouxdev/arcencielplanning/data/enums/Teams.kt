package com.aprouxdev.arcencielplanning.data.enums

import android.graphics.Color
import com.aprouxdev.arcencielplanning.R


enum class Teams {
    Clothes, Toys, Shop, Braderie, Cleaning, Other;

    fun getName(): String {
        return when(this) {
            Clothes -> "Vêtements"
            Toys -> "Jouets"
            Shop -> "Magasin"
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