package com.pahelukadam.pahelukadam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.admin.AdminEditActivity
import com.pahelukadam.pahelukadam.model.Adminbusinessidea

class AdminBusinessAdapter(
    private val businessList: List<Adminbusinessidea>
) : RecyclerView.Adapter<AdminBusinessAdapter.BusinessViewHolder>() {

    class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBusinessName: TextView = itemView.findViewById(R.id.tvBusinessName)
        val tvBudget: TextView = itemView.findViewById(R.id.tvBudget)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_business, parent, false)
        return BusinessViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val item = businessList[position]

        holder.tvBusinessName.text = "${position + 1}. ${item.businessName ?: "---"}"
        holder.tvBudget.text = "Budget: ${item.budget_range ?: "N/A"}"
        holder.tvCategory.text = "Category: ${item.category_name ?: "N/A"}"

        // Edit button click
        holder.ivEdit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AdminEditActivity::class.java)

            // **FIXED**: Pass the actual document ID from the model
            intent.putExtra("docId", item.id)
            intent.putExtra("businessName", item.businessName)
            intent.putExtra("description", item.description)
            // **FIXED**: Use a clear key for the category name
            intent.putExtra("categoryName", item.category_name)

            // **FIXED**: Dynamically split the budget range string
            val budgetParts = item.budget_range?.split(" - ") ?: listOf()
            val minBudget = budgetParts.getOrNull(0) ?: ""
            val maxBudget = budgetParts.getOrNull(1) ?: ""

            intent.putExtra("budgetMin", minBudget)
            intent.putExtra("budgetMax", maxBudget)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = businessList.size
}