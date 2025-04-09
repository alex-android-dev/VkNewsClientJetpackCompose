package com.example.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.NewsFeedRepository
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {

    //    private val mapper = NewsFeedMapper() // TODO в будущем будем инжектить
    private val repository = NewsFeedRepository()

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState


    init {
        loadRecommendations()
    }

    private fun loadRecommendations() {

        viewModelScope.launch {
            val feedPosts = repository.loadRecommendation()
            _screenState.value = NewsFeedScreenState.Posts(feedPosts)
        }

    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            if (feedPost.isLiked) {
                repository.deleteLike(feedPost)
            } else {
                repository.addLike(feedPost)
            }

            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }
    }

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