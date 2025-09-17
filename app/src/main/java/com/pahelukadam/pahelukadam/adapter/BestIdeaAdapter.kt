package com.pahelukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.R
import com.pahelukadam.pahelukadam.model.BestIdea

class BestIdeaAdapter(private val ideaList: List<BestIdea>) :
    RecyclerView.Adapter<BestIdeaAdapter.IdeaViewHolder>() {

    class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvBudget: TextView = itemView.findViewById(R.id.tvBudget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_best_idea, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = ideaList[position]
        holder.tvTitle.text = idea.title
        holder.tvCategory.text = "Category: ${idea.category}"
        holder.tvBudget.text = "Budget: ${idea.budget}"
    }

    override fun getItemCount() = ideaList.size
}
