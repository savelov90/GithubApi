package com.example.githubapi.data.db.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubapi.data.api_data.RepoResultItem
import com.example.githubapi.data.db.dao.RepoDAO

@Database(entities = [RepoResultItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDAO
}