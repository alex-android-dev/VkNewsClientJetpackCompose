package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.Repository

class DeleteLikeUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.deleteLike(feedPost)
    }
}