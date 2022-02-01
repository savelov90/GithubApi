package com.example.githubapi.data

import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.data.db.dao.RepoDAO
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.Executors

class MainRepository(private val repoDAO: RepoDAO) {

    fun putToDb(listRepoResult: MutableList<RepoResultItem>) {
        Executors.newSingleThreadExecutor().execute {
            repoDAO.insertAll(listRepoResult)
        }
    }

    fun getAllFromDB(): Single<List<RepoResultItem>> = repoDAO.getCachedNews()

    fun deleteAll() {
        Executors.newSingleThreadExecutor().execute {
            repoDAO.deleteAll()
        }
    }
}