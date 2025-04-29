package com.example.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel : ViewModel() {
    private val repository = Repository()
    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()
    private val recommendationsFlow = repository.recommendations

    /** Необходим, чтобы отлавливать ошибки при удалении и изменении лайка **/
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }

    /** Добавлять сюда catch или retry не имеет смысла, поскольку горячий флоу живет всегда **/
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