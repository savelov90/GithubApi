package com.example.githubapi.viewmodel


import androidx.lifecycle.ViewModel
import com.example.githubapi.App
import com.example.githubapi.data.api_data.RepoResultItem
import com.example.githubapi.interactor.Interactor
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class HomeFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun getRepos(since: String): Single<MutableList<RepoResultItem>> =
        interactor.getRepoFromApi(since)

    fun getRepoFromDB(): Single<List<RepoResultItem>> = interactor.getRepoFromDB()
}
