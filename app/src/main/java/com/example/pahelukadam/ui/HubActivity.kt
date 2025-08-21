package com.example.pahelukadam.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.pahelukadam.R
import com.example.pahelukadam.base.BaseScreen
import com.example.pahelukadam.databinding.ActivityHubBinding
import com.example.pahelukadam.ui.home.HomeHubFragment

class HubActivity : BaseScreen<ActivityHubBinding>() {

    override fun inflateBinding() = ActivityHubBinding.inflate(layoutInflater)

    override fun onReady() {
        // default tab
        switchTo(HomeHubFragment(), "home")

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchTo(HomeHubFragment(), "home")
                R.id.nav_explore -> switchTo(PlaceholderFragment.new("Explore coming soon"), "explore")
                R.id.nav_account -> switchTo(PlaceholderFragment.new("Account coming soon"), "account")
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
