package com.pahelukadam.pahelukadam.ui.home

import android.content.Intent
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

    private var _binding: FragmentHomeHubBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderAdapter: SliderAdapter
    private val handler = Handler(Looper.getMainLooper())

    private val sliderRunnable = Runnable {
        binding.imageSlider.currentItem++
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

        // ✅ Category card clicks
        binding.cardManufacturing.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ManufacturingFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardFood.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FoodBeverageFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardTech.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TechFragment())
                .addToBackStack(null)
                .commit()
        }

        // --- Image Slider Setup ---
        val images = listOf(
            R.drawable.t1,
            R.drawable.t10,
            R.drawable.t12,
            R.drawable.t15,
            R.drawable.t16,
            R.drawable.t3,
            R.drawable.t6,
            R.drawable.t9,
        )

        sliderAdapter = SliderAdapter(images) { imageView, resId ->
            Glide.with(this).load(resId).into(imageView)
        }

        binding.imageSlider.adapter = sliderAdapter
        binding.imageSlider.setPageTransformer(DepthPageTransformer())
        binding.imageSlider.setCurrentItem(Int.MAX_VALUE / 2, false)

        binding.imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    handler.removeCallbacks(sliderRunnable)
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    handler.postDelayed(sliderRunnable, 3000L)
                }
            }
        })

        // ✅ Start Now button (UI placeholder)
        binding.btnStartNow.setOnClickListener { }

        // ✅ Featured image
        Glide.with(this).load(R.drawable.sample_featured).into(binding.featuredImage)

        // ✅ View Details -> Launch BusinessDetailsActivity
        binding.btnViewDetails.setOnClickListener {
            val intent = Intent(requireContext(), BusinessDetailsActivity::class.java)
            startActivity(intent)
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
