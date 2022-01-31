package com.example.githubapi.data.api

import com.example.githubapi.data.api_data.RepoResult
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val REPO = "repositories"
private const val SINCE = "since"
private const val ID = "id"
private const val LOOKUP = "lookup"

interface RepoAPI {
    @GET(REPO)
    fun getRepo(
        @Query(SINCE) pagination: String,
    ): Single<RepoResult>

/*    @GET(LOOKUP)
    fun getTracks(
        @Query(ID) album: String,
        @Query(ENTITY) track: String
    ): Observable<SearchTracks>*/
}