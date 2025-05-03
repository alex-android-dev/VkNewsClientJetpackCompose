package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.NewsFeedResult
import com.example.vknewsclient.domain.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): StateFlow<NewsFeedResult> = repository.getRecommendations()
}