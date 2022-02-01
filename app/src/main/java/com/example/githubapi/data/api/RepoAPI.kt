package com.example.githubapi.data.api

import com.example.githubapi.data.api_data.commits.AllCommits
import com.example.githubapi.data.api_data.repos.RepoResult
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val REPO = "repositories"
private const val SINCE = "since"

interface RepoAPI {
    @GET(REPO)
    fun getRepo(
        @Query(SINCE) pagination: String,
    ): Single<RepoResult>

    @GET("repos/{login}/{name}/commits")
    fun getCommits(
        @Path("login") login: String,
        @Path("name") name: String
    ): Single<AllCommits>
}