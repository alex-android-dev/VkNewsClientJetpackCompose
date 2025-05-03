package com.example.vknewsclient.domain.usecase

import com.example.vknewsclient.domain.repository.Repository
import javax.inject.Inject

class LoadNextDataUseCase@Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() {
        repository.loadNextData()
    }
}