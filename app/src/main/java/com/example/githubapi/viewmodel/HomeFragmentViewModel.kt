package com.example.githubapi.viewmodel


import androidx.lifecycle.ViewModel
import com.example.githubapi.App
import com.example.githubapi.data.api_data.repos.RepoResultItem
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

    fun deleteFromDB() = interactor.deleteFromDB()

    fun savePositionToPreferences(position: Int) {
        interactor.savePositionToPreferences(position)
    }

    fun getPositionFromPreferences(): Int = interactor.getPositionFromPreferences()

    fun savePaginationID(id: Int) {
        interactor.savePaginationID(id)
    }

    fun getPaginationID(): Int = interactor.getPaginationID()

    fun saveLaunch(launch: Boolean) {
        interactor.saveLaunch(launch)
    }

    fun getLaunch(): Boolean {
        return interactor.getLaunch()
    }
}
