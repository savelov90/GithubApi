package com.example.githubapi.di

import com.example.githubapi.di.modules.DatabaseModule
import com.example.githubapi.di.modules.DomainModule
import com.example.githubapi.di.modules.RemoteModule
import com.example.githubapi.viewmodel.DetailsFragmentViewModel
import com.example.githubapi.viewmodel.HomeFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)

interface AppComponent {
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
    fun inject(detailsFragmentViewModel: DetailsFragmentViewModel)
}