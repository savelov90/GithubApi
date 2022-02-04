package com.example.githubapi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
private const val LAST_POS_STR = 0
private const val PAGINATION_ID_STR = 0

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recycler: RecyclerView
    private lateinit var repoAdapter: RepoListRecyclerAdapter
    private lateinit var layout: LinearLayoutManager
    private val autoDisposable = AutoDisposable()
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(HomeFragmentViewModel::class.java)
    }
    private var launch = true
    private var isLoading = false
    private var lastPosition = LAST_POS_STR
    private var paginationID = PAGINATION_ID_STR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = false
        autoDisposable.bindTo(lifecycle)
        initPullToRefresh()
        recycler = initRecyckler()
        initRecycklerScroll()
        paginationID = viewModel.getPaginationID()
        lastPosition = viewModel.getPositionFromPreferences()

        if (launch) {
            lastPosition = LAST_POS_STR
            getReposFromApi(PAGINATION_ID_STR.toString())
            launch = false
        } else {
            getLastSavedRepo()
        }

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
        clearResources()
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
                        isLoading = true
                        getReposFromApi(paginationID.toString())
                    }
                }
            }
        })
    }

    private fun initPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            recycler.isVisible = false
            clearResources()
            getReposFromApi(PAGINATION_ID_STR.toString())
            binding.pullToRefresh.isRefreshing = false
            recycler.isVisible = true
        }
    }

    private fun getReposFromApi(since: String) {
        binding.progressBar.isVisible = true
        val allRepos = viewModel.getRepos(since)
        allRepos.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    if (lastPosition < LAST_POS_STR.plus(9)) getLastSavedRepo()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_home_api),
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoading = false
                    binding.progressBar.isVisible = false
                },
                onSuccess = { list ->
                    repoAdapter.addItems(list)
                    if (lastPosition != LAST_POS_STR) {
                        recycler.scrollToPosition(lastPosition)
                    }
                    val lastID = list.last().id
                    setPaginationID(lastID)
                    isLoading = false
                    binding.progressBar.isVisible = false
                },
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
                    if (lastPosition != LAST_POS_STR) {
                        recycler.scrollToPosition(lastPosition)
                    }
                }
            )
            .addTo(autoDisposable)
    }

    private fun clearResources() {
        repoAdapter.items.clear()
        viewModel.savePaginationID(PAGINATION_ID_STR)
        viewModel.savePositionToPreferences(LAST_POS_STR)
        viewModel.deleteFromDB()
    }
}