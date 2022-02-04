package com.example.githubapi.view.rv_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.R
import com.example.githubapi.view.rv_viewholders.ParentsViewHolder

class ParentsAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val items = mutableListOf<String>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ParentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ParentsViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    fun addItems(list: List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}