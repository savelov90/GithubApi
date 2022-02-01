package com.example.githubapi.data.api_data.repos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
    val avatar_url: String,
    val login: String
) : Parcelable