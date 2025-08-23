package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.databinding.FragmentHomeHubBinding

class HomeHubFragment : Fragment() {

    // ✅ FIX: Binding class must start with uppercase (matches XML file name fragment_home_hub.xml → FragmentHomeHubBinding)
    private var _binding: FragmentHomeHubBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderAdapter: SliderAdapter
    private val handler = Handler(Looper.getMainLooper())

    private val sliderRunnable = object : Runnable {
        override fun run() {
            val vp = binding.imageSlider
            if (sliderAdapter.itemCount > 0) {
                val next = (vp.currentItem + 1) % sliderAdapter.itemCount
                vp.setCurrentItem(next, true)
                handler.postDelayed(this, 3000L)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeHubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Category clicks
        binding.cardManufacturing.setOnClickListener { /* TODO open list */ }
        binding.cardFood.setOnClickListener { /* TODO open list */ }
        binding.cardTech.setOnClickListener { /* TODO open list */ }

        // Image slider
        val images = listOf(
            R.drawable.sample_slider_1,
            R.drawable.sample_slider_2,
            R.drawable.sample_slider_3
        )

        sliderAdapter = SliderAdapter(images) { imageView, resId ->
            Glide.with(imageView).load(resId).into(imageView)
        }

        binding.imageSlider.adapter = sliderAdapter
        binding.imageSlider.offscreenPageLimit = 1
        binding.imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})

        // Smart suggestion button
        binding.btnStartNow.setOnClickListener {
            // TODO: navigate to budget/category screen
        }

        // Featured business
        Glide.with(this).load(R.drawable.sample_featured).into(binding.featuredImage)
        binding.btnViewDetails.setOnClickListener {
            // TODO: open details
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sliderRunnable, 3000L)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sliderRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
