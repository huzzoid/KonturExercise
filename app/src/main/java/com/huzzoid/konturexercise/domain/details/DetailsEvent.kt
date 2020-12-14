package com.huzzoid.konturexercise.domain.details

sealed class DetailsEvent {
    data class Error(val error: Throwable) : DetailsEvent()
}
