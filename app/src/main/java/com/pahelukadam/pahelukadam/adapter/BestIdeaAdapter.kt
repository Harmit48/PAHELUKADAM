package com.pahelukadam.pahelukadam.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.databinding.ItemBestIdeaBinding
import com.pahelukadam.pahelukadam.model.BestIdea

class BestIdeaAdapter(
    private var ideas: List<BestIdea>,
    private val onItemClick: (BestIdea) -> Unit
) : RecyclerView.Adapter<BestIdeaAdapter.IdeaViewHolder>() {

    class IdeaViewHolder(
        private val binding: ItemBestIdeaBinding,
        private val onItemClick: (BestIdea) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(idea: BestIdea) {
            binding.tvTitle.text = idea.businessName
            binding.tvCategory.text = idea.category_name
            binding.tvBudget.text = idea.budget_range

            binding.root.setOnClickListener {
                onItemClick(idea)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val binding = ItemBestIdeaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IdeaViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        holder.bind(ideas[position])
    }

    override fun getItemCount() = ideas.size

    fun updateIdeas(newIdeasList: List<BestIdea>) {
        this.ideas = newIdeasList
        notifyDataSetChanged()
    }
}
