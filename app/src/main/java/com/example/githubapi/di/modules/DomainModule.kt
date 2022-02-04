package com.example.githubapi.di.modules

import android.content.Context
import com.example.githubapi.data.MainRepository
import com.example.githubapi.data.api.RepoAPI
import com.example.githubapi.data.preference.PreferenceProvider
import com.example.githubapi.interactor.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Singleton
    @Provides
    //Создаем экземпляр SharedPreferences
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Provides
    @Singleton
    fun provideInteractor(
        repository: MainRepository,
        repoAPI: RepoAPI,
        preferenceProvider: PreferenceProvider
    ) =
        Interactor(repo = repository, retrofitService = repoAPI, preferences = preferenceProvider)
}