package com.example.githubapi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubapi.App
import com.example.githubapi.data.api_data.commits.AllCommitsItem
import com.example.githubapi.interactor.Interactor
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class DetailsFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    fun getCommit(login: String, fullName: String): Single<AllCommitsItem> = interactor.getCommitsFromApi(login, fullName)
}
