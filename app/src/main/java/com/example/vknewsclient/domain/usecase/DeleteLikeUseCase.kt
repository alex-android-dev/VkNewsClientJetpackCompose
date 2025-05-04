package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.repository.Repository
import javax.inject.Inject

class DeleteLikeUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.deleteLike(feedPost)
    }
}