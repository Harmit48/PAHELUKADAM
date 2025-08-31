package com.pahelukadam.pahelukadam.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pahekukadam.pahelukadam.R

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_admin)

        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById(R.id.fabAdd)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // 🔹 For now, just dummy data list
        val dummyData = listOf("Business 1", "Business 2", "Business 3")
        recyclerView.adapter = SimpleAdapter(dummyData)

        // FAB click action
        fabAdd.setOnClickListener {
            Toast.makeText(this, "Add Business clicked!", Toast.LENGTH_SHORT).show()

            // Example: Open new activity
            // val intent = Intent(this, AddBusinessActivity::class.java)
            // startActivity(intent)
        }
    }
}
