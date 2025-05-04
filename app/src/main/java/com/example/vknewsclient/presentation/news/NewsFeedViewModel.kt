package com.example.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.RepositoryImpl
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.NewsFeedResult
import com.example.vknewsclient.domain.usecase.AddLikeUseCase
import com.example.vknewsclient.domain.usecase.DeleteLikeUseCase
import com.example.vknewsclient.domain.usecase.GetRecommendationsUseCase
import com.example.vknewsclient.domain.usecase.LoadNextDataUseCase
import com.example.vknewsclient.domain.usecase.RemovePostUseCase
import com.example.vknewsclient.extensions.mergeWith
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel @Inject constructor(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val removePostUseCase: RemovePostUseCase,
) : ViewModel() {
    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    private val recommendationsFlow = getRecommendationsUseCase.invoke().map {
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
    val screenState = getRecommendationsUseCase.invoke()
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
                Log.d("NewsFeedViewModel", "loadNextRecommendations")
                loadNextDataFlow.emit(
                    NewsFeedScreenState.Posts(
                        posts = recommendationsFlow.posts,
                        nextDataIsLoading = true
                    )
                )
                loadNextDataUseCase.invoke()
            }
        }
    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch(exceptionHandler) {
            if (feedPost.isLiked) {
                deleteLikeUseCase.invoke(feedPost)
            } else {
                addLikeUseCase.invoke(feedPost)
            }
        }
        // TODO при многократном нажатии приложение падает
        // https://stepik.org/lesson/874315/step/1?unit=878711
    }

    // TODO BUG. Пост не исчезает после удаления. Остается место. Список не обновляется.
    fun removePost(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            removePostUseCase.invoke(feedPost)
        }
    }
}