package com.example.githubapi.data.api_data.repos

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "search_repo", indices = [Index(value = ["repo_name"], unique = true)])
data class RepoResultItem(
    @PrimaryKey(autoGenerate = true) var ids: Int = 0,
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "repo_name", defaultValue = "неизвестен") var full_name: String,
    @ColumnInfo(name = "name", defaultValue = "неизвестен") var name: String,
    @Embedded val owner: @RawValue Owner
) : Parcelable