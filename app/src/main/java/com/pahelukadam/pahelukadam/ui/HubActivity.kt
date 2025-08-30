package com.example.pahelukadam.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pahelukadam.R
import com.example.pahelukadam.base.BaseScreen
import com.example.pahelukadam.databinding.ActivityHubBinding
import com.example.pahelukadam.ui.home.HomeHubFragment
import com.example.pahelukadam.ui.home.ExploreFragment
import com.example.pahelukadam.ui.account.AccountFragment  // ✅ Import your new fragment

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
