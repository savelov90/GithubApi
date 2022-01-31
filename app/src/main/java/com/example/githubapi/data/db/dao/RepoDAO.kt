package com.example.githubapi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubapi.data.api_data.RepoResultItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface RepoDAO {
    @Query("SELECT * FROM search_repo")
    fun getCachedNews(): Single<List<RepoResultItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: MutableList<RepoResultItem>)

    @Query("DELETE FROM search_repo")
    fun deleteAll()
}