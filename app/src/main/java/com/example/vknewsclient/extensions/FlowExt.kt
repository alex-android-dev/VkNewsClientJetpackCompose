package com.example.vknewsclient.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

fun <T> Flow<T>.mergeWith(flow: Flow<T>): Flow<T> {
    return merge(this, flow)
}