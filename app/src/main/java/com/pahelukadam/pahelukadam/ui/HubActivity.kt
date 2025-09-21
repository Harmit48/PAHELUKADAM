package com.pahelukadam.pahelukadam.ui

import androidx.fragment.app.Fragment
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.base.BaseScreen
import com.pahelukadam.pahelukadam.databinding.ActivityHubBinding
import com.pahelukadam.pahelukadam.ui.home.HomeHubFragment
import com.pahelukadam.pahelukadam.ui.home.ExploreFragment
import com.pahelukadam.pahelukadam.ui.account.AccountFragment

class HubActivity : BaseScreen<ActivityHubBinding>() {

    // ✅ Keep fragments as single instances
    private val homeFragment = HomeHubFragment()
    private val exploreFragment = ExploreFragment()
    private val accountFragment = AccountFragment()

    private var activeFragment: Fragment = homeFragment

    override fun inflateBinding() = ActivityHubBinding.inflate(layoutInflater)

    override fun onReady() {
        // ✅ Add all fragments once and hide the inactive ones
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, accountFragment, "account").hide(accountFragment)
            .add(R.id.fragmentContainer, exploreFragment, "explore").hide(exploreFragment)
            .add(R.id.fragmentContainer, homeFragment, "home")
            .commit()

        activeFragment = homeFragment

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchTo(homeFragment)
                R.id.nav_explore -> switchTo(exploreFragment)
                R.id.nav_account -> switchTo(accountFragment)
            }
            true
        }
    }

    private fun switchTo(fragment: Fragment) {
        if (fragment == activeFragment) return // ✅ Avoid reloading same fragment

        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()

        activeFragment = fragment
    }
}
