package com.example.leostoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.leostoryapp.data.remote.response.ListStoryItem
import com.example.leostoryapp.data.repository.StoryRepository

class StoryViewModel(
    storyRepository: StoryRepository
) : ViewModel() {

    val getStories: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)
}

