package com.pahekukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahekukadam.pahelukadam.R
import com.pahekukadam.pahelukadam.model.Adminbusinessidea

class AdminBusinessAdapter(
    private val businessList: List<Adminbusinessidea>
) : RecyclerView.Adapter<AdminBusinessAdapter.BusinessViewHolder>() {

    class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBusinessName: TextView = itemView.findViewById(R.id.tvBusinessName)
        val tvBudget: TextView = itemView.findViewById(R.id.tvBudget)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_business, parent, false)
        return BusinessViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val item = businessList[position]
        holder.tvBusinessName.text = "${position + 1}. ${item.businessName ?: "---"}"
        holder.tvBudget.text = "Budget: ${item.budgetID ?: "N/A"}"
        holder.tvCategory.text = "Category: ${item.categoryID ?: "N/A"}"
    }

    override fun getItemCount(): Int = businessList.size
}
