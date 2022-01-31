package com.example.githubapi.di.modules

import android.content.Context
import androidx.room.Room
import com.example.githubapi.data.MainRepository
import com.example.githubapi.data.db.dao.RepoDAO
import com.example.githubapi.data.db.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Singleton
    @Provides
    fun provideRepoDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "repo_db"
        ).build().repoDao()

    @Provides
    @Singleton
    fun provideRepository(repoDAO: RepoDAO) = MainRepository(repoDAO)
}