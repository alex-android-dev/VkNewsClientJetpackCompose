package com.example.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.FeedPost
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NewsFeedViewModel : ViewModel() {
    private val repository = Repository()
    private val mutex = Mutex()

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState>
        get() = _screenState


    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            repository
                .recommendations
                .filter { it.isNotEmpty() }
                .collect {
                    _screenState.value =
                        NewsFeedScreenState.Posts(posts = it, nextDataIsLoading = false)
                }

        }
    }

    fun loadNextRecommendations() {
        _screenState.value =
            NewsFeedScreenState.Posts(posts = repository.feedPosts, nextDataIsLoading = true)

        viewModelScope.launch {
            repository.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch {
            mutex.withLock {
                if (feedPost.isLiked) {
                    repository.deleteLike(feedPost)
                } else {
                    repository.addLike(feedPost)
                }

                _screenState.value = NewsFeedScreenState.Posts(repository.feedPosts)
            }

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