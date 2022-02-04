package com.example.githubapi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.api_data.commits.AllCommitsItem
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.databinding.FragmentDetailsBinding
import com.example.githubapi.disposable.AutoDisposable
import com.example.githubapi.disposable.addTo
import com.example.githubapi.view.MainActivity
import com.example.githubapi.view.rv_adapters.ParentsAdapter
import com.example.githubapi.viewmodel.DetailsFragmentViewModel
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers


private const val KEY = "repo"

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var parentsAdapter: ParentsAdapter
    private val autoDisposable = AutoDisposable()
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoDisposable.bindTo(lifecycle)
        initRecyclerParent()
        setRepoDetails()
    }

    private fun setRepoDetails() {
        val repo = arguments?.getParcelable<RepoResultItem>(MainActivity.KEY)
        repo?.let { getCommitFromApi(it.owner.login, it.name) }

        if (repo != null) {
            Picasso.get()
                .load(repo.owner.avatar_url)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.detailsAvatar)
        } else {
            binding.detailsAvatar.setImageResource(R.drawable.white)
        }

        binding.detailsLogin.text = repo?.owner?.login
        binding.detailsRepoName.text = repo?.name
    }

    private fun getCommitFromApi(login: String, fullName: String) {
        val commit = viewModel.getCommit(login, fullName)
        commit.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_detail_api),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                onSuccess = {
                    setCommitDetails(it)
                }
            )
            .addTo(autoDisposable)
    }

    private fun setCommitDetails(commitsItem: AllCommitsItem) {
        binding.detailsCommitMessage.text = commitsItem.commit.message
        binding.detailsCommiterName.text = commitsItem.commit.author.name
        binding.detailsCommitDate.text = editData(commitsItem.commit.author.date)
        val listParents = mutableListOf<String>()
        commitsItem.parents.forEach {
            listParents.add(it.sha)
        }
        parentsAdapter.addItems(listParents)
    }

    private fun initRecyclerParent() {
        binding.parentsRecycler.apply {
            parentsAdapter = ParentsAdapter()
            adapter = parentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun editData(data: String): String {
        val edit = data.toCharArray()
        val string =
            "${edit[8]}" + "${edit[9]}" + "." + "${edit[5]}" + "${edit[6]}" + "." + "${edit[0]}" + "${edit[1]}" + "${edit[2]}" + "${edit[3]}"
        return string
    }
}