package com.example.githubapi.data

import com.example.githubapi.data.api_data.RepoResultItem
import com.example.githubapi.data.db.dao.RepoDAO

class MainRepository(private val repoDAO: RepoDAO) {

    fun putToDb(resultAlbums: List<RepoResultItem>) {
            repoDAO.insertAll(resultAlbums)
    }

/*    fun getAllFromDB(): Observable<List<RepoResult>> = repoDAO.getCachedNews()

    fun deleteAll() = repoDAO.deleteAll()*/
}