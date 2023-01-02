package com.aprouxdev.arcencielplanning.views

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.enums.Teams

interface TeamButtonCallback {
    fun onTeamButtonClicked(team: Teams)
}

class TeamsButton @JvmOverloads constructor(
    val teams: Teams,
    val listener: TeamButtonCallback,
    val isTeamSelected: Boolean,
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_teams_button, this)
        setupUiViews()
    }

    private fun setupUiViews() {
        val teamText = teams.getName()
        val teamTextView = findViewById<AppCompatTextView>(R.id.team_text)
        val teamColorRes = teams.getColorRes()
        teamTextView.apply {
            text = teamText
        }
        val checkIcon = findViewById<AppCompatImageView>(R.id.team_check_icon)
        checkIcon.isVisible = isTeamSelected
        val button = findViewById<RelativeLayout>(R.id.team_button)
        button.apply {
            val buttonDrawable = background
            buttonDrawable.setColorFilter(ContextCompat.getColor(context, teamColorRes), PorterDuff.Mode.SRC_ATOP)
            background = buttonDrawable
            setOnClickListener { listener.onTeamButtonClicked(teams) }
        }


    }
}

