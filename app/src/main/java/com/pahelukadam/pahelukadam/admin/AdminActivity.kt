package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pahelukadam.pahelukadam.R

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.adminContainer, AdminFragment())
                .commit()
        }
    }
}
