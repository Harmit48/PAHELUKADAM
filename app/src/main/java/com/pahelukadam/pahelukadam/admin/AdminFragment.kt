package com.pahelukadam.pahelukadam.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pahelukadam.pahelukadam.databinding.FragmentAdminBinding
import com.pahelukadam.pahelukadam.adapter.AdminBusinessAdapter
import com.pahelukadam.pahelukadam.model.Adminbusinessidea
import com.google.firebase.firestore.FirebaseFirestore

class AdminFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdminBusinessAdapter
    private val db = FirebaseFirestore.getInstance()
    private val businessList = mutableListOf<Adminbusinessidea>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        adapter = AdminBusinessAdapter(businessList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        loadBusinessIdeas()

        // ✅ Floating Action Button Click → Open AddIdea Activity
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AdminAddideaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadBusinessIdeas() {
        db.collection("business_ideas")
            .get()
            .addOnSuccessListener { result ->
                businessList.clear()
                for (doc in result) {
                    val idea = doc.toObject(Adminbusinessidea::class.java)
                    businessList.add(idea)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    override fun onResume() {
        super.onResume()
        // 🔄 Refresh list when returning from AddIdea screen
        loadBusinessIdeas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
