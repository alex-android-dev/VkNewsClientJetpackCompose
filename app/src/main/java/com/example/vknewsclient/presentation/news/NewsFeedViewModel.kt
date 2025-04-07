package com.example.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import kotlin.random.Random

class NewsFeedViewModel : ViewModel() {

    private val postInitialList by lazy {
        mutableListOf<FeedPost>().apply {
            repeat(Random.Default.nextInt(10, 20)) {
                val feedPost = FeedPost(id = it)
                add(
                    feedPost
                )
            }
        }
    }

    private val initialState = NewsFeedScreenState.Posts(postInitialList)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState

    fun removePost(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldFeedPostList = currentState.posts.toMutableList()

        val feedPostForDelete = oldFeedPostList.find { it.id == feedPost.id }
        oldFeedPostList.remove(feedPostForDelete)
        _screenState.value = NewsFeedScreenState.Posts(oldFeedPostList)
    }

    fun updateStatisticCard(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldFeedPostList = currentState.posts.toMutableList()
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

        val newFeedPosts = oldFeedPostList.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }

        _screenState.value = NewsFeedScreenState.Posts(newFeedPosts)
    }
}