package com.aprouxdev.arcencielplanning

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.aprouxdev.arcencielplanning.databinding.ActivityMainBinding
import com.aprouxdev.arcencielplanning.extensions.present
import com.aprouxdev.arcencielplanning.fragments.HomeFragment
import com.aprouxdev.arcencielplanning.fragments.PlanningFragment
import com.aprouxdev.arcencielplanning.modals.NewEventModal


class MainActivity : AppCompatActivity() {

    companion object {
        private const val BottomNavSelectedColor = R.color.colorPrimaryLight
        private const val BottomNavUnselectedColor = R.color.greys_300
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var mHomeFragment: HomeFragment
    private lateinit var mPlanningFragment: PlanningFragment
    private lateinit var mActiveFragment: Fragment
    private var mCurrentTab = MainTab.Home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTabButton(mCurrentTab)
        initializedFragments()
        setupBottomNavigationViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //region UI METHODS

    private fun initializedFragments() {
        mHomeFragment = HomeFragment.newInstance()
        mPlanningFragment = PlanningFragment.newInstance()
        mActiveFragment = mHomeFragment
    }

    private fun setupBottomNavigationViews() {
        // Add all fragments to fragment manager and hide not active ones
        with(supportFragmentManager) {
            beginTransaction().add(
                R.id.main_fragment_container,
                mPlanningFragment,
                PlanningFragment.TAG
            ).hide(mPlanningFragment).commit()
            beginTransaction().add(
                R.id.main_fragment_container,
                mHomeFragment,
                HomeFragment.TAG
            ).commit()
        }
        with(binding) {
            tabHomeButton.setOnClickListener {
                setTab(MainTab.Home)
            }
            tabPlanningButton.setOnClickListener {
                setTab(MainTab.Planning)
            }
        }
    }

    private fun setTab(tab: MainTab) {
        if (mCurrentTab == tab) return
        setTabButton(tab)
        val newFragment = when (tab) {
            MainTab.Home -> mHomeFragment
            MainTab.Planning -> mPlanningFragment
        }
        supportFragmentManager.beginTransaction()
            .hide(mActiveFragment).show(newFragment).commit()
        mActiveFragment = newFragment
        mCurrentTab = tab

    }

    private fun setTabButton(tab: MainTab) {
        val homeColor =
            if (tab == MainTab.Home) BottomNavSelectedColor else BottomNavUnselectedColor
        val planningColor =
            if (tab == MainTab.Planning) BottomNavSelectedColor else BottomNavUnselectedColor

        with(binding) {
            val homeIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_menu_home)
                ?.setTintColor(ContextCompat.getColor(this@MainActivity, homeColor))
            tabHomeIcon.setImageDrawable(homeIcon)
            tabHomeText.setTextColor(ContextCompat.getColor(this@MainActivity, homeColor))

            val planningIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_menu_calendar)
                ?.setTintColor(ContextCompat.getColor(this@MainActivity, planningColor))
            tabPlanningIcon.setImageDrawable(planningIcon)
            tabPlanningText.setTextColor(ContextCompat.getColor(this@MainActivity, planningColor))

            addEventButton.setOnClickListener {
                val addEventModal = NewEventModal.newInstance()
                addEventModal.present(supportFragmentManager, NewEventModal.TAG)
            }
        }
    }
}

enum class MainTab { Home, Planning }

fun Drawable.setTintColor(color: Int): Drawable {
    val wrappedDrawable = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrappedDrawable, color)
    return wrappedDrawable
}