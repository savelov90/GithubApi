package com.example.githubapi.data.api_data.commits

data class AllCommitsItem(
    val commit: Commit,
    val parents: List<Parent>
)