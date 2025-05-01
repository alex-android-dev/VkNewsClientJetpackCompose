package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.repository.Repository

class LoadNextDataUseCase(
    private val repository: Repository
) {
    suspend operator fun invoke() {
        repository.loadNextData()
    }
}