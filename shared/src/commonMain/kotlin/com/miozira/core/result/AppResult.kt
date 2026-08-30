package com.miozira.core.result

sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>

    data class Failure(val error: AppError) : AppResult<Nothing>
}

sealed interface AppError {
    val message: String

    data class InvalidState(override val message: String) : AppError

    data class Unavailable(override val message: String) : AppError

    data class Unexpected(override val message: String) : AppError
}
