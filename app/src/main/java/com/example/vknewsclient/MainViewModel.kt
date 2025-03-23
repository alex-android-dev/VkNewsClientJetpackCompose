package com.example.vknewsclient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(50) {
            add(
                FeedPost(
                    id = it
                )
            )
        }
    }

    private val _feedPostsLiveData = MutableLiveData<List<FeedPost>>(initialList)
    val feedPostsLiveData: LiveData<List<FeedPost>>
        get() = _feedPostsLiveData

    fun removePost(feedPost: FeedPost) {
        Log.d("MainViewModel", "fun delete")
        Log.d("MainViewModel", _feedPostsLiveData.value?.size.toString())
        val oldFeedPostList = feedPostsLiveData.value?.toMutableList() ?: mutableListOf()
        oldFeedPostList.remove(feedPost)
        _feedPostsLiveData.value = oldFeedPostList
        Log.d("MainViewModel", _feedPostsLiveData.value?.size.toString())
        Log.d("MainViewModel", "${_feedPostsLiveData.value}")
    }

    fun updateStatisticCard(feedPost: FeedPost, statisticItem: StatisticItem) {

        val oldFeedPostList = feedPostsLiveData.value?.toMutableList() ?: mutableListOf()

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

        _feedPostsLiveData.value = oldFeedPostList.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }

        Log.d("MainViewModel", "${_feedPostsLiveData.value?.get(0)?.statistics}")
        Log.d("MainViewModel", "${_feedPostsLiveData.value}")

    }
}