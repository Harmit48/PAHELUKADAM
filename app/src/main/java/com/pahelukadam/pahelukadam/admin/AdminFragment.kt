package com.pahelukadam.pahelukadam.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.adapter.AdminBusinessAdapter
import com.pahelukadam.pahelukadam.model.Adminbusinessidea

class AdminFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminBusinessAdapter
    private val businessIdeasList = mutableListOf<Adminbusinessidea>()

    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()
        listenForDataChanges()

        // ✅ JUST LINK THE FAB TO AddIdeaActivity
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddIdeaActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = AdminBusinessAdapter(businessIdeasList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun listenForDataChanges() {
        val collectionRef = db.collection("business_ideas").orderBy("businessName", Query.Direction.ASCENDING)

        listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("AdminFragment", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                businessIdeasList.clear()
                for (document in snapshot.documents) {
                    val idea = document.toObject(Adminbusinessidea::class.java)
                    if (idea != null) {
                        businessIdeasList.add(idea)
                    }
                }
                adapter.notifyDataSetChanged()
            } else {
                Log.d("AdminFragment", "Current data: null")
                businessIdeasList.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration?.remove()
    }
}
