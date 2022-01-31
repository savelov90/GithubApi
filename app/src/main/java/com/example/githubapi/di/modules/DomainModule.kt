package com.example.githubapi.di.modules

import com.example.githubapi.data.MainRepository
import com.example.githubapi.data.api.RepoAPI
import com.example.githubapi.interactor.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule() {

    @Provides
    @Singleton
    fun provideInteractor(
        repository: MainRepository,
        repoAPI: RepoAPI
    ) =
        Interactor(
            repo = repository,
            retrofitService = repoAPI
        )
}