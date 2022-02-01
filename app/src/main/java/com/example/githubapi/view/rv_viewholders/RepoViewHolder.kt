package com.example.githubapi.view.rv_viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.R
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.databinding.RepoItemBinding
import com.squareup.picasso.Picasso


class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val repoItemBinding = RepoItemBinding.bind(itemView)
    private val name = repoItemBinding.ownerName
    private val repo = repoItemBinding.repoName
    private val picture = repoItemBinding.picture

    fun bind(repoResultItem: RepoResultItem) {
        name.text = repoResultItem.owner.login
        repo.text = repoResultItem.full_name

        if (repoResultItem.owner.avatar_url.isEmpty()) {
            picture.setImageResource(R.drawable.white)
        } else {
            Picasso.get()
                .load(repoResultItem.owner.avatar_url)
                .error(android.R.drawable.stat_notify_error)
                .into(picture)
        }
    }
}