package com.example.pahelukadam.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pahelukadam.databinding.ItemSliderBinding

class SliderAdapter(
    private val items: List<Int>,
    private val loader: (ImageView, Int) -> Unit
) : RecyclerView.Adapter<SliderAdapter.SVH>() {

    inner class SVH(val vb: ItemSliderBinding) : RecyclerView.ViewHolder(vb.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SVH {
        return SVH(ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SVH, position: Int) {
        loader(holder.vb.slideImage, items[position])
    }
}
