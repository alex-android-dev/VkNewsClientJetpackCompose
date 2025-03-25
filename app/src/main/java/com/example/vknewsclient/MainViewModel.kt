package com.example.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.FeedPost
import com.example.vknewsclient.domain.HomeScreenState
import com.example.vknewsclient.domain.PostComment
import com.example.vknewsclient.domain.StatisticItem
import kotlin.random.Random
import kotlin.random.nextInt

class MainViewModel : ViewModel() {

    private val postInitialList by lazy {
        mutableListOf<FeedPost>().apply {
            repeat(10) {
                add(
                    FeedPost(
                        id = it
                    )
                )
            }
        }
    }

    private val postCommentsList by lazy {
        mutableListOf<PostComment>().apply {
            repeat(25) {
                add(
                    PostComment(
                        id = it,
                        authorName = "author number $it",
                    )
                )
            }
        }.toList()
    }

    private val initialState = HomeScreenState.Posts(postInitialList)

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState

    private var savedState: HomeScreenState? = initialState

    fun showComments(feedPost: FeedPost) {
        savedState = _screenState.value

        val currentState = screenState.value
        if (currentState !is HomeScreenState.Posts) return

        _screenState.value =
            HomeScreenState.Comments(post = feedPost, comments = postCommentsList)
    }

    fun closeComments() {
        if (savedState != null) _screenState.value = savedState
    }

    fun removePost(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is HomeScreenState.Posts) return

        val oldFeedPostList = currentState.posts.toMutableList()

        val feedPostForDelete = oldFeedPostList.find { it.id == feedPost.id }
        oldFeedPostList.remove(feedPostForDelete)
        _screenState.value = HomeScreenState.Posts(oldFeedPostList)
    }

    fun updateStatisticCard(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is HomeScreenState.Posts) return

        val oldFeedPostList = currentState.posts.toMutableList()
        val oldFeedPostStatistics = feedPost.statistics

        val newStatistics = oldFeedPostStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }

        val newFeedPost = feedPost.copy(
            statistics = newStatistics
        )

        val newFeedPosts = oldFeedPostList.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }

        _screenState.value = HomeScreenState.Posts(newFeedPosts)
    }
}