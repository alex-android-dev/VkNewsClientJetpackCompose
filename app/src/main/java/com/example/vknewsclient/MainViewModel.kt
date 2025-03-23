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
    val feedPostsLiveData = _feedPostsLiveData as LiveData<List<FeedPost>>

    fun deletePost(feedPost: FeedPost) {
        Log.d("MainViewModel", "fun delete")
        Log.d("MainViewModel", _feedPostsLiveData.value?.size.toString())
        val feedPostList = _feedPostsLiveData.value?.toMutableList() ?: mutableListOf()
        feedPostList.remove(feedPost)
        _feedPostsLiveData.value = feedPostList
        Log.d("MainViewModel", _feedPostsLiveData.value?.size.toString())

    }

    fun updateStatisticCard(feedPost: FeedPost, statisticItem: StatisticItem) {

        val feedPostList = _feedPostsLiveData.value?.toMutableList() ?: mutableListOf()

        feedPostList.forEachIndexed { index, post ->
            if (post == feedPost) {

                val newStatistic = post.statistics.toMutableList().apply {
                    replaceAll { oldItem ->
                        if (oldItem.type == statisticItem.type) {
                            Log.d("MainViewModel", "oldItem: ${oldItem.type}")
                            Log.d("MainViewModel", "statisticItem: ${statisticItem.type}")

                            oldItem.copy(count = oldItem.count + 1)
                        } else oldItem
                    }
                }

                feedPostList[index] = post.copy(
                    statistics = newStatistic
                )

            }

        }

        _feedPostsLiveData.value = feedPostList

        Log.d("MainViewModel", "${_feedPostsLiveData.value?.get(0)?.statistics}")

    }
}