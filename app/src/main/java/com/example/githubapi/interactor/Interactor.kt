package com.example.githubapi.interactor


import com.example.githubapi.data.MainRepository
import com.example.githubapi.data.api.RepoAPI
import com.example.githubapi.data.api_data.RepoResultItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: RepoAPI
) {

    fun getRepoFromApi(since: String): Single<MutableList<RepoResultItem>> = retrofitService.getRepo(since)
            .subscribeOn(Schedulers.io())
            .map { result ->
                val list = mutableListOf<RepoResultItem>()
                result.forEach {
                    list.add(it)
                }
                list
            }
            .doAfterSuccess {
                repo.deleteAll()
                repo.putToDb(it)
            }

    fun getRepoFromDB(): Single<List<RepoResultItem>> = repo.getAllFromDB()

/*    fun getTracksFromApi(albumId: String): Observable<List<String>> = retrofitService
        .getTracks(albumId, TRACK_ENTITY)
        .subscribeOn(Schedulers.io())
        .map {
            it.results
        }
        .map { list ->
            val tracksNameList = mutableListOf<String>()
            list.forEach { tracksNameList.add(it.trackName) }
            tracksNameList
        }*/
}