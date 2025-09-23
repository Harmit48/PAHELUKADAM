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
import com.pahelukadam.pahelukadam.databinding.FragmentExploreBinding
import com.pahelukadam.pahelukadam.model.BusinessIdea

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val ideaList = mutableListOf<BusinessIdea>()
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        // 🔥 Hide content initially and start logo animation
        binding.contentLayout.visibility = View.GONE
        binding.logoLoader.visibility = View.VISIBLE
        startLogoAnimation()

        setupRecyclerView()
        fetchIdeas()

        return binding.root
    }

    private fun setupRecyclerView() {
        businessAdapter = BusinessAdapter(ideaList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = businessAdapter
        }
    }

    private fun fetchIdeas() {
        db.collection("explore")
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
                Log.e("ExploreFragment", "Error getting documents: ", exception)
                showContent() // Show UI even if data fails
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
