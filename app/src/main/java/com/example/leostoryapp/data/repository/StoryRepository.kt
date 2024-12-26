package com.example.leostoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.ExperimentalPagingApi
import androidx.paging.liveData
import com.example.leostoryapp.data.local.database.StoryDatabase
import com.example.leostoryapp.data.pref.UserPreference
import com.example.leostoryapp.data.remote.response.ListStoryItem
import com.example.leostoryapp.data.remote.retrofit.ApiService

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, userPreference),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
    }
}
