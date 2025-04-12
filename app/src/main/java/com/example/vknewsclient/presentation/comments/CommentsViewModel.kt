package com.example.vknewsclient.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vknewsclient.data.repository.Repository
import com.example.vknewsclient.domain.FeedPost
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
) : ViewModel() {

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState

    private val repository = Repository()


    init {
        _screenState.value = CommentsScreenState.Loading
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {

        viewModelScope.launch {
            val comments = repository.loadCommentsToPost(feedPost)

            _screenState.value =
                CommentsScreenState.Comments(post = feedPost, comments = comments)
        }


    }


}