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
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.databinding.FragmentHomeBinding
import com.example.githubapi.disposable.AutoDisposable
import com.example.githubapi.disposable.addTo
import com.example.githubapi.view.MainActivity
import com.example.githubapi.view.rv_adapters.RepoListRecyclerAdapter
import com.example.githubapi.view.rv_adapters.TopSpacingItemDecoration
import com.example.githubapi.viewmodel.HomeFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

private const val PADDING = 10
private const val SINCE = "0"

class HomeFragment : Fragment() {

    private lateinit var repoAdapter: RepoListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding
    private val autoDisposable = AutoDisposable()
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoDisposable.bindTo(lifecycle)
        initRecyckler()
        getReposFromApi()
    }

    private fun initRecyckler() {
        binding.mainRecycler.apply {
            repoAdapter =
                RepoListRecyclerAdapter(object : RepoListRecyclerAdapter.OnItemClickListener {
                    override fun click(repoResultItem: RepoResultItem) {
                        (requireActivity() as MainActivity).launchDetailsFragment(repoResultItem)
                    }
                })
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(PADDING)
            addItemDecoration(decorator)
        }
    }

    private fun getReposFromApi() {
        val allRepos = viewModel.getRepos(SINCE)
        allRepos.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    getLastSavedRepo()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_home_api),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSuccess = {
                    repoAdapter.addItems(it)
                }
            )
            .addTo(autoDisposable)
    }

    private fun getLastSavedRepo() {
        val repoDB = viewModel.getRepoFromDB()
        repoDB.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_home_db),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSuccess = {
                    repoAdapter.addItems(it)
                }
            )
            .addTo(autoDisposable)
    }
}