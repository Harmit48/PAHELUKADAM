package com.pahelukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.databinding.ItemBestIdeaBinding // Import the binding class
import com.pahelukadam.pahelukadam.model.BestIdea

class BestIdeaAdapter(private var ideas: List<BestIdea>) :
    RecyclerView.Adapter<BestIdeaAdapter.IdeaViewHolder>() {

    // 1. Update the ViewHolder to use the generated ItemBestIdeaBinding class
    class IdeaViewHolder(private val binding: ItemBestIdeaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(idea: BestIdea) {
            // Use the correct IDs from your XML via the binding object
            binding.tvTitle.text = idea.businessName
            binding.tvCategory.text = idea.category_name
            binding.tvBudget.text = idea.budget_range
        }
    }

    // 2. Update onCreateViewHolder to inflate using the binding class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val binding = ItemBestIdeaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IdeaViewHolder(binding)
    }

    // 3. Update onBindViewHolder to call the new bind function
    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        holder.bind(ideas[position])
    }

    override fun getItemCount() = ideas.size

    fun updateIdeas(newIdeasList: List<BestIdea>) {
        this.ideas = newIdeasList
        notifyDataSetChanged()
    }
}