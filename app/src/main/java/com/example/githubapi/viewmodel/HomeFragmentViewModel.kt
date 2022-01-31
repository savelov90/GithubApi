package com.example.githubapi.viewmodel


import androidx.lifecycle.ViewModel
import com.example.githubapi.App
import com.example.githubapi.data.api_data.RepoResultItem
import com.example.githubapi.interactor.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class HomeFragmentViewModel : ViewModel() {
    //val albumsListData: Single<List<RepoResult>>

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
        //albumsListData = interactor.getAlbumsFromDB()
    }

    fun getRepos(since: String): Single<MutableList<RepoResultItem>> = interactor.getRepoFromApi(since)
}
