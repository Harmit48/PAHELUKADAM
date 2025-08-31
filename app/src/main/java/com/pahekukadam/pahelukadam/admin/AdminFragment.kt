package com.pahekukadam.pahelukadam.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pahekukadam.pahelukadam.databinding.FragmentAdminBinding
import com.pahekukadam.pahelukadam.adapter.BusinessAdapter
import com.pahekukadam.pahelukadam.model.BusinessIdea
import com.google.firebase.firestore.FirebaseFirestore

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BusinessAdapter
    private val db = FirebaseFirestore.getInstance()
    private val businessList = mutableListOf<BusinessIdea>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BusinessAdapter(businessList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        loadBusinessIdeas()

        // Floating Add Button
        binding.fabAdd.setOnClickListener {
            // TODO: open AddBusinessActivity or Dialog
        }
    }

    private fun loadBusinessIdeas() {
        db.collection("business_ideas")
            .get()
            .addOnSuccessListener { result ->
                businessList.clear()
                for (doc in result) {
                    val idea = doc.toObject(BusinessIdea::class.java)
                    businessList.add(idea)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // handle error
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
