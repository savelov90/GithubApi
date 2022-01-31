package com.example.githubapi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubapi.data.api_data.RepoResultItem
import io.reactivex.rxjava3.core.Observable

@Dao
interface RepoDAO {
/*    @Query("SELECT * FROM search_albums")
    fun getCachedNews(): Observable<List<ResultAlbums>>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<RepoResultItem>)

/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(resultAlbums: ResultAlbums)

    @Query("DELETE FROM search_albums")
    fun deleteAll()*/
}