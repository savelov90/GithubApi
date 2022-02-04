package com.example.githubapi.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.databinding.ParentItemBinding

class ParentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var parentItemBinding = ParentItemBinding.bind(itemView)
    var category = parentItemBinding.textViewSmall

    fun bind(string: String) {
        category.text = string
    }
}