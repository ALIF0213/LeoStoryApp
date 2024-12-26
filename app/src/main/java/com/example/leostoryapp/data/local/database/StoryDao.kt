package com.example.leostoryapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.leostoryapp.data.remote.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getStories(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM stories")
    suspend fun clearStories()
}

