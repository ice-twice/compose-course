package com.jetreader.domain.common

import kotlinx.coroutines.CancellationException

inline fun <R> runSuspendCatching(block: () -> R): Result<R> =
    try {
        Result.success(block())
    } catch (e: Exception) {
        if (e is CancellationException) {
            throw e
        }
        Result.failure(e)
    }
