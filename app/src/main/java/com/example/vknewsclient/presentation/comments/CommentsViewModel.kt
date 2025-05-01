package com.example.vknewsclient.presentation.comments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.entity.FeedPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
) : ViewModel() {
    private val repository = Repository()

    private val _state = MutableStateFlow<CommentsScreenState>(CommentsScreenState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.emit(CommentsScreenState.Loading)

            repository.loadCommentsToPost(feedPost)

            repository.commentsToPost
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