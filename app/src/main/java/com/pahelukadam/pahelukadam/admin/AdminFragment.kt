package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // **FIXED**: Changed R.id.adminRecyclerView to R.id.recyclerView to match your XML
        recyclerView = view.findViewById(R.id.recyclerView)
        setupRecyclerView()
        listenForDataChanges()
    }

    private fun setupRecyclerView() {
        // We need to use 'requireContext()' in Fragments for the context
        adapter = AdminBusinessAdapter(businessIdeasList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    /**
     * Attaches a listener that automatically updates the list
     * when data changes in Firestore.
     */
    private fun listenForDataChanges() {
        val collectionRef = db.collection("business_ideas").orderBy("businessName", Query.Direction.ASCENDING)

        listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("AdminFragment", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                businessIdeasList.clear()
                // Convert all documents to our Adminbusinessidea object
                for (document in snapshot.documents) {
                    val idea = document.toObject(Adminbusinessidea::class.java)
                    if (idea != null) {
                        businessIdeasList.add(idea)
                    }
                }
                // Notify the adapter that the data has changed to refresh the UI
                adapter.notifyDataSetChanged()
            } else {
                Log.d("AdminFragment", "Current data: null")
                businessIdeasList.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * Removes the listener when the fragment is destroyed to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration?.remove()
    }
}