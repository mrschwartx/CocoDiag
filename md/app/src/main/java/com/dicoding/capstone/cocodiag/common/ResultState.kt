package com.dicoding.capstone.cocodiag.common

import com.dicoding.capstone.cocodiag.data.remote.payload.ErrorResponse

sealed class ResultState<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: ErrorResponse) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}