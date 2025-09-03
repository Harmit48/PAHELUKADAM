package com.pahelukadam.pahelukadam.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pahelukadam.pahelukadam.databinding.SliderItemBinding

class SliderAdapter(
    private val imageList: List<Int>,
    private val onImageLoad: (imageView: ImageView, resId: Int) -> Unit
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(private val binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageResId: Int) {
            onImageLoad(binding.sliderImageView, imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = SliderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        // Use the modulo operator to loop through the image list
        val realPosition = position % imageList.size
        holder.bind(imageList[realPosition])
    }

    // Return a huge number to simulate infinity
    override fun getItemCount(): Int = if (imageList.isNotEmpty()) Int.MAX_VALUE else 0
}