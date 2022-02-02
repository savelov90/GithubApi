package com.example.githubapi

import android.app.Application
import com.example.githubapi.di.AppComponent
import com.example.githubapi.di.DaggerAppComponent
import com.example.githubapi.di.modules.DatabaseModule
import com.example.githubapi.di.modules.DomainModule
import com.example.githubapi.di.modules.RemoteModule

class App : Application() {

    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}