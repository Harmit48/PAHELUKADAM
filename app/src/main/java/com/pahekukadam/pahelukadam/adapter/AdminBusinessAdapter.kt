package com.pahekukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahekukadam.pahelukadam.R
import com.pahekukadam.pahelukadam.model.BusinessIdea
import java.text.NumberFormat
import java.util.Locale

class AdminBusinessAdapter(
    private val ideas: List<BusinessIdea>,
    private val onEditClick: (BusinessIdea) -> Unit // callback for edit
) : RecyclerView.Adapter<AdminBusinessAdapter.AdminBusinessViewHolder>() {

    inner class AdminBusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvAdminTitle)
        val category: TextView = itemView.findViewById(R.id.tvAdminCategory)
        val budget: TextView = itemView.findViewById(R.id.tvAdminBudget)
        val editIcon: ImageView = itemView.findViewById(R.id.ivEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBusinessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_business, parent, false)
        return AdminBusinessViewHolder(view)
    }

    override fun getItemCount(): Int = ideas.size

    override fun onBindViewHolder(holder: AdminBusinessViewHolder, position: Int) {
        val currentIdea = ideas[position]

        // Numbered title
        holder.title.text = "${position + 1}. ${currentIdea.title}"

        // Format budget
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val minBudget = formatter.format(currentIdea.budget["min"] ?: 0)
        val maxBudget = formatter.format(currentIdea.budget["max"] ?: 0)
        holder.budget.text = "Budget: $minBudget – $maxBudget"

        // Category
        holder.category.text = "Category: ${currentIdea.category}"

        // Edit button click
        holder.editIcon.setOnClickListener {
            onEditClick(currentIdea)
        }
    }
}
