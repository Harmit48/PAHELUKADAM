package com.pahelukadam.pahelukadam.ui

import androidx.fragment.app.Fragment
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.base.BaseScreen
import com.pahelukadam.pahelukadam.databinding.ActivityHubBinding
import com.pahelukadam.pahelukadam.ui.home.HomeHubFragment
import com.pahelukadam.pahelukadam.ui.home.ExploreFragment
import com.pahelukadam.pahelukadam.ui.account.AccountFragment

class HubActivity : BaseScreen<ActivityHubBinding>() {

    override fun inflateBinding() = ActivityHubBinding.inflate(layoutInflater)

    override fun onReady() {
        // Set default fragment to Home
        switchTo(HomeHubFragment(), "home")

        // Handle bottom navigation item clicks
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchTo(HomeHubFragment(), "home")
                R.id.nav_explore -> switchTo(ExploreFragment(), "explore")
                R.id.nav_account -> switchTo(AccountFragment(), "account")
            }
            true
        }
    }

    private fun switchTo(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }
}
