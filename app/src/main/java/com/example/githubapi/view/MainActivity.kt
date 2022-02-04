package com.example.githubapi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapi.R
import com.example.githubapi.data.api_data.repos.RepoResultItem
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.view.fragments.DetailsFragment
import com.example.githubapi.view.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .commit()
    }

    fun launchDetailsFragment(repoResultItem: RepoResultItem) {
        val bundle = Bundle()
        bundle.putParcelable(KEY, repoResultItem)
        val fragment = DetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val KEY = "repo"
    }
}