package com.example.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class NewsFeedViewModel : ViewModel() {
    private val repository = Repository()

    private val recommendationsFlow = repository.recommendations

//    private val loadNextDataEvents = MutableSharedFlow<Unit>()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

//    private val loadNextDataFlow = flow {
//        loadNextDataEvents.collect {
//
//        }
//    }

    val screenState = repository
        .recommendations
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )


            repository.loadNextData()
        }
    }
//    private fun loadRecommendations() {
//        viewModelScope.launch {
//            repository
//                .recommendations
//                .filter { it.isNotEmpty() }
//                .collect {
//                    _screenState.value =
//                        NewsFeedScreenState.Posts(posts = it, nextDataIsLoading = false)
//                }
//
//        }
//    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch {
            if (feedPost.isLiked) {
                repository.deleteLike(feedPost)
            } else {
                repository.addLike(feedPost)
            }


        }
        // TODO при многократном нажатии приложение падает
        // https://stepik.org/lesson/874315/step/1?unit=878711
    }

    fun removePost(feedPost: FeedPost) {
        if (screenState !is NewsFeedScreenState.Posts) return

        viewModelScope.launch {
            repository.removePost(feedPost)
        }

    }
}