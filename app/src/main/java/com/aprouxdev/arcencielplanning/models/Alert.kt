package com.aprouxdev.arcencielplanning.models

import com.aprouxdev.arcencielplanning.R

enum class AlertType(val text: Int, val icon: Int, val color: Int, val background: Int, val chevronIcon: Int) {
    General(text= R.string.alert_generale, icon= R.drawable.ic_alert_info, color= R.color.myOrange, background = R.drawable.background_alert_container_generale, chevronIcon= R.drawable.ic_alert_chevron_generale),
    Team(text= R.string.alert_team, icon= R.drawable.ic_alert_team, color= R.color.alert_blue, background = R.drawable.background_alert_container_team, chevronIcon= R.drawable.ic_alert_chevron_team),
    Shop(text= R.string.alert_shop, icon= R.drawable.ic_alert_shopping, color= R.color.alert_green, background = R.drawable.background_alert_container_shop, chevronIcon= R.drawable.ic_alert_chevron_shop),
    Urgency(text= R.string.alert_urgency, icon= R.drawable.ic_alert_urgency, color= R.color.alert_red, background = R.drawable.background_alert_container_urgency, chevronIcon= R.drawable.ic_alert_chevron_urgency),
    Assiduity(text= R.string.alert_assiduity, icon= R.drawable.ic_alert_assiduity, color= R.color.alert_yellow, background = R.drawable.background_alert_container_assiduity, chevronIcon= R.drawable.ic_alert_chevron_assiduity)

}
data class Alert(
    val id:Int,
    val type: AlertType,
    val title: String,
    val body: String,
    val endDate: String
)