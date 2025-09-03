package com.pahelukadam.pahelukadam.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pahelukadam.pahelukadam.adapter.BusinessAdapter
import com.pahelukadam.pahelukadam.databinding.FragmentTechBinding
import com.pahelukadam.pahelukadam.model.BusinessIdea

class TechFragment : Fragment() {

    private var _binding: FragmentTechBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val ideaList = mutableListOf<BusinessIdea>()
    private lateinit var businessAdapter: BusinessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTechBinding.inflate(inflater, container, false)
        setupRecyclerView()
        fetchTechIdeas()
        return binding.root
    }

    private fun setupRecyclerView() {
        businessAdapter = BusinessAdapter(ideaList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = businessAdapter
        }
    }

    private fun fetchTechIdeas() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        db.collection("explore")
            .whereEqualTo("category", "Tech")
            .get()
            .addOnSuccessListener { result ->
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                ideaList.clear()
                for (document in result) {
                    val idea = document.toObject(BusinessIdea::class.java)
                    ideaList.add(idea)
                }
                businessAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Log.e("TechFragment", "Error getting documents: ", exception)
                Toast.makeText(context, "Error fetching ideas.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}