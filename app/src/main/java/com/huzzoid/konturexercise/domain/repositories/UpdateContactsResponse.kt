package com.huzzoid.konturexercise.domain.repositories

data class UpdateContactsResponse(val errors: List<Throwable> = emptyList())

fun UpdateContactsResponse.isPartialError() = errors.isNotEmpty() && errors.size < 3

fun UpdateContactsResponse.isFullError() = errors.size > 2
