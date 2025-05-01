package com.example.vknewsclient.presentation.comments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.RepositoryImpl
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.usecase.GetCommentsToPostUseCase
import com.example.vknewsclient.domain.usecase.LoadCommentsToPostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
) : ViewModel() {
    private val repositoryImpl = RepositoryImpl()
    private val getCommentsToPostUseCase = GetCommentsToPostUseCase(repositoryImpl)
    private val loadCommentsUseCase = LoadCommentsToPostUseCase(repositoryImpl)

    private val _state = MutableStateFlow<CommentsScreenState>(CommentsScreenState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.emit(CommentsScreenState.Loading)

            loadCommentsUseCase.invoke(feedPost)

            getCommentsToPostUseCase.invoke()
                .filter { commentsList ->
                    commentsList.isNotEmpty()
                }
                .collect { commentsList ->
                    Log.d("CommentsViewModel", "Comments: $commentsList")
                    _state.emit(CommentsScreenState.Comments(commentsList))
                }
        }
    }


}