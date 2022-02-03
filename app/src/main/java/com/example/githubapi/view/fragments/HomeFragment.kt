package com.example.githubapi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class HomeFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var repoAdapter: RepoListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding
    private var lastPosition = 0
    private var paginationID = 0
    lateinit var layout: LinearLayoutManager
    var repos = mutableListOf<RepoResultItem>()
    var launch = true
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
        recycler = initRecyckler()
        initRecycklerScroll()
        paginationID = viewModel.getPaginationID()

        if (launch) {
            getReposFromApi(paginationID.toString())
            launch = false
        } else {
            getLastSavedRepo()
        }

        binding.button.setOnClickListener {
            getReposFromApi(paginationID.toString())
        }

        lastPosition = viewModel.getPositionFromPreferences()
        recycler.scrollToPosition(lastPosition)
    }

    override fun onResume() {
        super.onResume()
        recycler.scrollToPosition(lastPosition)
    }

    override fun onPause() {
        super.onPause()
        viewModel.savePositionToPreferences(lastPosition)
        viewModel.savePaginationID(paginationID)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.savePaginationID(0)
        viewModel.savePositionToPreferences(0)
        viewModel.deleteFromDB()
    }

    private fun initRecyckler(): RecyclerView {
        return binding.mainRecycler.apply {
            repoAdapter =
                RepoListRecyclerAdapter(object : RepoListRecyclerAdapter.OnItemClickListener {
                    override fun click(repoResultItem: RepoResultItem) {
                        (requireActivity() as MainActivity).launchDetailsFragment(repoResultItem)
                    }
                })
            repoAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            layout = layoutManager as LinearLayoutManager
            val decorator = TopSpacingItemDecoration(PADDING)
            addItemDecoration(decorator)
        }
    }

    private fun initRecycklerScroll() {
        var isLoading = false
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                lastPosition = layout.findFirstCompletelyVisibleItemPosition()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as RecyclerView.LayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItems =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount) {

                        getReposFromApi(paginationID.toString())
                    }
                }
            }
        })
    }

    private fun getReposFromApi(since: String) {
        val allRepos = viewModel.getRepos(since)
        allRepos.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    if (lastPosition < 10) getLastSavedRepo()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_home_api),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSuccess = { list ->
                    repoAdapter.addItems(list)
                    if (lastPosition != 0) {
                        recycler.scrollToPosition(lastPosition)
                    }
                    val lastID = list.last().id
                    setPaginationID(lastID)
                    println(lastID)
                }
            )
            .addTo(autoDisposable)
    }

    private fun setPaginationID(id: Int) {
        paginationID = id
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
                    if (lastPosition != 0) {
                        recycler.scrollToPosition(lastPosition)
                    }
                }
            )
            .addTo(autoDisposable)
    }
}