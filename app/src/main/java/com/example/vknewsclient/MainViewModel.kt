package com.example.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val _feedPost = MutableLiveData(FeedPost())
    val feedPost = _feedPost as LiveData<FeedPost>


    fun updateCount(item: StatisticItem) {

        val oldStatistics = feedPost.value?.statistics ?: throw IllegalStateException()

        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type)
                    oldItem.copy(count = oldItem.count + 1)
                else oldItem
            }
        }


        _feedPost.value = feedPost.value?.copy(statistics = newStatistics)

    }
}