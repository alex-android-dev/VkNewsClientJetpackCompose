package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.repository.Repository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsToPostUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): StateFlow<List<PostComment>> = repository.getCommentsToPost()
}