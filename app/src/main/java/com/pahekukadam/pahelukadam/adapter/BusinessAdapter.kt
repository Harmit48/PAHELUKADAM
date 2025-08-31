package com.pahekukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahekukadam.pahelukadam.R
import com.pahekukadam.pahelukadam.model.BusinessIdea
import java.text.NumberFormat
import java.util.Locale

class BusinessAdapter(private val ideas: List<BusinessIdea>) :
    RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder>() {

    // This class holds the views for a single list item.
    inner class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val category: TextView = itemView.findViewById(R.id.tvCategory)
        val budget: TextView = itemView.findViewById(R.id.tvBudget)
    }

    // Creates a new ViewHolder by inflating the layout.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_business, parent, false)
        return BusinessViewHolder(view)
    }

    // Returns the total number of items in the list.
    override fun getItemCount(): Int = ideas.size

    // Binds the data from your list to the views in the ViewHolder.
    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val currentIdea = ideas[position]

        holder.title.text = currentIdea.title
        holder.category.text = "Category: ${currentIdea.category}"

        // Format the budget numbers nicely
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val minBudget = formatter.format(currentIdea.budget["min"] ?: 0)
        val maxBudget = formatter.format(currentIdea.budget["max"] ?: 0)

        holder.budget.text = "Budget: $minBudget - $maxBudget"
    }
}