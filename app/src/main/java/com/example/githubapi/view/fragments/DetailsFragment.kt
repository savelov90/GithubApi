package com.example.githubapi.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.githubapi.R
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.databinding.FragmentDetailsBinding
import com.example.githubapi.viewmodel.DetailsFragmentViewModel
import com.example.githubapi.disposable.AutoDisposable
import com.squareup.picasso.Picasso

private const val KEY = "repo"

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    //private lateinit var allTracks: Observable<List<String>>
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
        setAlbumsDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun setAlbumsDetails() {
        val repo = arguments?.getParcelable<RepoResultItem>(KEY)

        /*allTracks = viewModel.getTracks(album.collectionId.toString())
        allTracks.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_details),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                onNext = {
                    setTrackNames(it)
                }
            )
            .addTo(autoDisposable)*/

        if (repo != null) {
            Picasso.get()
                .load(repo.owner.avatar_url)
                .error(android.R.drawable.stat_notify_error)
                .into(binding.detailsAvatar)
        } else {
            binding.detailsAvatar.setImageResource(R.drawable.white)
        }

        binding.detailsLogin.text = repo?.owner?.login
        binding.detailsRepoName.text = repo?.full_name
    }

/*    private fun setTrackNames(list: List<String>) {
        val tracksNameList = mutableListOf<String>()
        for (i in list.indices) {
            if (list[i] != null) {
                tracksNameList.add(i.toString() + "   " + list[i])
            }
        }
        binding.detailsTracks.text = tracksNameList.joinToString("\n")
    }*/
}