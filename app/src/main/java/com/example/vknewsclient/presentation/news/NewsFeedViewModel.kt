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
    private val repository = NewsFeedRepository()

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState


    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            val feedPosts = repository.loadRecommendation()
            _screenState.value =
                NewsFeedScreenState.Posts(posts = feedPosts, nextDataIsLoading = false)
        }
    }

    fun loadNextRecommendations() {
        _screenState.value =
            NewsFeedScreenState.Posts(posts = repository.feedPosts, nextDataIsLoading = true)

        loadRecommendations()
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

        // TODO при многократном нажатии приложение падает
        // https://stepik.org/lesson/874315/step/1?unit=878711
    }

    fun removePost(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        viewModelScope.launch {
            repository.removePost(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
        }

    }
}