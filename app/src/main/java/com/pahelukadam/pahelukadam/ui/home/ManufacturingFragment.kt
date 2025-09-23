package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.adapter.BusinessAdapter
import com.pahelukadam.pahelukadam.databinding.FragmentManufacturingBinding
import com.pahelukadam.pahelukadam.model.BusinessIdea

class ManufacturingFragment : Fragment() {

    private var _binding: FragmentManufacturingBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val ideaList = mutableListOf<BusinessIdea>()
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManufacturingBinding.inflate(inflater, container, false)

        // 🔥 Hide content initially and start logo animation
        binding.contentLayout.visibility = View.GONE
        binding.logoLoader.visibility = View.VISIBLE
        startLogoAnimation()

        setupRecyclerView()
        fetchManufacturingIdeas()

        return binding.root
    }

    private fun setupRecyclerView() {
        businessAdapter = BusinessAdapter(ideaList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = businessAdapter
        }
    }

    private fun fetchManufacturingIdeas() {
        db.collection("explore")
            .whereEqualTo("category", "Manufacturing")
            .get()
            .addOnSuccessListener { result ->
                ideaList.clear()
                for (document in result) {
                    val idea = document.toObject(BusinessIdea::class.java)
                    ideaList.add(idea)
                }
                businessAdapter.notifyDataSetChanged()
                showContent()
            }
            .addOnFailureListener { exception ->
                Log.e("ManufacturingFragment", "Error getting documents: ", exception)
                showContent() // show content anyway to avoid stuck loader
            }
    }

    private fun startLogoAnimation() {
        val logo = binding.logoLoader
        logo.animate()
            .alpha(0f)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                logo.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        if (logo.visibility == View.VISIBLE) startLogoAnimation()
                    }
                    .start()
            }
            .start()
    }

    private fun showContent() {
        binding.logoLoader.clearAnimation()
        binding.logoLoader.visibility = View.GONE
        binding.contentLayout.alpha = 0f
        binding.contentLayout.visibility = View.VISIBLE
        binding.contentLayout.animate()
            .alpha(1f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
