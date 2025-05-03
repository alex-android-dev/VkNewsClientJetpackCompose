package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.Repository

class AddLikeUseCase (
    private val repository: Repository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.addLike(feedPost)
    }
}