package com.example.githubapi.interactor


import com.example.githubapi.data.MainRepository
import com.example.githubapi.data.api.RepoAPI
import com.example.githubapi.data.api_data.commits.AllCommitsItem
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.newspaper.data.Preference.PreferenceProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.FieldPosition

private const val ITEM = 0

class Interactor(
    private val repo: MainRepository,
    private val retrofitService: RepoAPI,
    private val preferences: PreferenceProvider
) {

    fun getRepoFromApi(since: String): Single<MutableList<RepoResultItem>> = retrofitService
        .getRepo(since)
        .subscribeOn(Schedulers.io())
        .map { result ->
            val list = mutableListOf<RepoResultItem>()
            result.forEach {
                list.add(it)
            }
            list
        }
        .doAfterSuccess {
            repo.putToDb(it)
        }

    fun getRepoFromDB(): Single<List<RepoResultItem>> = repo.getAllFromDB()

    fun getCommitsFromApi(login: String, fullName: String): Single<AllCommitsItem> = retrofitService
        .getCommits(login, fullName)
        .subscribeOn(Schedulers.io())
        .map { it[ITEM] }

    fun deleteFromDB() = repo.deleteAll()

    fun savePositionToPreferences(position: Int) {
        preferences.saveRecyclerPosition(position)
    }

    fun getPositionFromPreferences(): Int = preferences.getRecyclerPosition()
}