package com.example.vknewsclient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.HomeScreenState
import com.example.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val postInitialList = mutableListOf<FeedPost>().apply {
        repeat(50) {
            add(
                FeedPost(
                    id = it
                )
            )
        }
    }

    private val initialState = HomeScreenState.Posts(postInitialList)

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState

    fun removePost(feedPost: FeedPost) {
        val oldFeedPostList = screenState.value.toMutableList() ?: mutableListOf()
        val feedPostForDelete = oldFeedPostList.find { it.id == feedPost.id }
        oldFeedPostList.remove(feedPostForDelete)
        _screenState.value = oldFeedPostList
    }

    fun updateStatisticCard(feedPost: FeedPost, statisticItem: StatisticItem) {

        val oldFeedPostList = screenState.value?.toMutableList() ?: mutableListOf()

        val oldFeedPostStatistics = feedPost.statistics

        val newStatistics = oldFeedPostStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }

        val newFeedPost = feedPost.copy(
            statistics = newStatistics
        )

        _screenState.value = oldFeedPostList.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }

        Log.d("MainViewModel", "${_screenState.value?.get(0)?.statistics}")
        Log.d("MainViewModel", "${_screenState.value}")

    }
}