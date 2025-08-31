package com.pahekukadam.pahelukadam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pahekukadam.pahelukadam.databinding.FragmentExploreBinding
import com.pahekukadam.pahelukadam.model.BusinessIdea
import com.pahekukadam.pahelukadam.adapter.BusinessAdapter

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val ideaList = mutableListOf<BusinessIdea>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fetchIdeas()

        return binding.root
    }

    private fun fetchIdeas() {
        db.collection("explore") // Firestore collection name
            .get()
            .addOnSuccessListener { result ->
                ideaList.clear()
                for (document in result) {
                    val idea = document.toObject(BusinessIdea::class.java)
                    ideaList.add(idea)
                }
                binding.recyclerView.adapter = BusinessAdapter(ideaList)
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
