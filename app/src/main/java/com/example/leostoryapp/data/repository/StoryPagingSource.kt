package com.example.leostoryapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.leostoryapp.data.local.database.StoryDao
import com.example.leostoryapp.data.remote.response.ListStoryItem
import com.example.leostoryapp.data.remote.retrofit.ApiService
import java.lang.Exception

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String,
    private val storyDao: StoryDao
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getStories("$token", page, params.loadSize)
            val stories = response.body()?.listStory ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
