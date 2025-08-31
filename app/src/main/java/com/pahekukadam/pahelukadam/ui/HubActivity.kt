package com.pahekukadam.pahelukadam.ui

import androidx.fragment.app.Fragment
import com.pahekukadam.pahelukadam.R
import com.pahekukadam.pahelukadam.base.BaseScreen
import com.pahekukadam.pahelukadam.databinding.ActivityHubBinding
import com.pahekukadam.pahelukadam.ui.home.HomeHubFragment
import com.pahekukadam.pahelukadam.ui.home.ExploreFragment
import com.pahekukadam.pahelukadam.ui.account.AccountFragment  // ✅ Import your new fragment

class HubActivity : BaseScreen<ActivityHubBinding>() {

    override fun inflateBinding() = ActivityHubBinding.inflate(layoutInflater)

    override fun onReady() {
        // ✅ Default tab
        switchTo(HomeHubFragment(), "home")

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchTo(HomeHubFragment(), "home")
                R.id.nav_explore -> switchTo(ExploreFragment(), "explore")
                R.id.nav_account -> switchTo(AccountFragment(), "account") // ✅ Added Account tab
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



