package com.pahelukadam.pahelukadam.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseScreen<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    abstract fun inflateBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding()
        setContentView(binding.root)
        onReady()
    }

    open fun onReady() { /* for children */ }
}
