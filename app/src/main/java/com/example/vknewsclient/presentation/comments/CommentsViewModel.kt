package com.example.vknewsclient.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.PostComment
import kotlin.random.Random

class CommentsViewModel(
    feedPost: FeedPost,
) : ViewModel() {

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState


    init {
        loadComments(feedPost)
    }

    fun loadComments(feedPost: FeedPost) {
        val comments = createFakeCommentsList()

        _screenState.value =
            CommentsScreenState.Comments(post = feedPost, comments = comments)
    }

    private fun createFakeCommentsList(): List<PostComment> {
        return mutableListOf<PostComment>().apply {
            repeat(
                Random.nextInt(10, 30)
            ) {
                add(
                    PostComment(
                        id = it,
                        authorName = "author number $it",
                    )
                )
            }
        }.toList()
    }


}