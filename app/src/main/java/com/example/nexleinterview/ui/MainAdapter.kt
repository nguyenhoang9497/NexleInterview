package com.example.nexleinterview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexleinterview.databinding.ItemBinding
import com.example.nexleinterview.ui.base.BaseAdapter

class MainAdapter : BaseAdapter() {

    var onItemClick: (Int, String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.onBind(items[position] as String)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) {
            binding.root.setOnClickListener {
                onItemClick.invoke(adapterPosition, item)
            }
            binding.text.text = item
        }
    }
}