package com.aprouxdev.arcencielplanning.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import com.aprouxdev.arcencielplanning.R


class TeamsTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_teams_text_view, this)
    }

    fun setName(name: String) {
        findViewById<AppCompatTextView>(R.id.team_text).text = name
    }
}