package com.example.githubapi.data.api_data.commits

data class AllCommitsItem(
    val author: Author,
    val comments_url: String,
    val commit: Commit,
    val committer: CommitterX,
    val html_url: String,
    val node_id: String,
    val parents: List<Parent>,
    val sha: String,
    val url: String
)