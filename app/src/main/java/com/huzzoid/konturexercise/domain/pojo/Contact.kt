package com.huzzoid.konturexercise.domain.pojo

import java.util.*

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val height: String,
    val biography: String,
    val temperament: String,
    val start: Date,
    val end: Date
)
