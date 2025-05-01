package com.example.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.NewsFeedResult
import com.example.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {
    private val repository = Repository()

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    private val recommendationsFlow = repository.recommendationPosts.map {
        when (it) {
            is NewsFeedResult.Error -> {
                Log.d("NewsFeedViewModel", "NewsFeedResult.Error")
                NewsFeedScreenState.Error as NewsFeedScreenState
            }

            is NewsFeedResult.Success -> {
                Log.d("NewsFeedViewModel", "NewsFeedResult.Success")
                NewsFeedScreenState.Posts(posts = it.posts) as NewsFeedScreenState
            }
        }
    }

    /** Необходим, чтобы отлавливать ошибки при удалении и изменении лайка **/
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }

    /** Добавлять сюда catch или retry не имеет смысла, поскольку горячий флоу живет всегда **/
    val screenState = repository
        .recommendationPosts
        .map {
            if (it is NewsFeedResult.Success) {
                NewsFeedScreenState.Posts(posts = it.posts) as NewsFeedScreenState
            } else {
                Log.d("NewsFeedViewModel", "Screen State is Error")
                NewsFeedScreenState.Error
            }
        }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)


    fun loadNextRecommendations() {
        viewModelScope.launch {
            if (recommendationsFlow is NewsFeedResult.Success) {
                loadNextDataFlow.emit(
                    NewsFeedScreenState.Posts(
                        posts = recommendationsFlow.posts,
                        nextDataIsLoading = true
                    )
                )
                repository.loadNextData()
            } else {
                loadNextDataFlow.emit(
                    NewsFeedScreenState.Error
                )
            }
        }
    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch(exceptionHandler) {
            if (feedPost.isLiked) {
                repository.deleteLike(feedPost)
            } else {
                repository.addLike(feedPost)
            }
        }
        // TODO при многократном нажатии приложение падает
        // https://stepik.org/lesson/874315/step/1?unit=878711
    }

    // TODO BUG. Пост не исчезает после удаления. Остается место. Список не обновляется.
    fun removePost(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            repository.removePost(feedPost)
        }

    }
}