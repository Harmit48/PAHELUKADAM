package com.pahekukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahekukadam.pahelukadam.R
import com.pahekukadam.pahelukadam.model.BusinessIdea

class BusinessAdapter(private val ideaList: List<BusinessIdea>) :
    RecyclerView.Adapter<BusinessAdapter.BusinessViewHolder>() {

    class BusinessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleText)
        val category: TextView = itemView.findViewById(R.id.categoryText)
        val budget: TextView = itemView.findViewById(R.id.budgetText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_business, parent, false)
        return BusinessViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val idea = ideaList[position]
        holder.title.text = idea.title
        holder.category.text = idea.category
        holder.budget.text = "Budget: ₹${idea.budget.min} - ₹${idea.budget.max}"
    }

    override fun getItemCount(): Int = ideaList.size
}
