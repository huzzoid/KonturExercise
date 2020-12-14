package com.huzzoid.konturexercise.domain.details

import com.huzzoid.konturexercise.domain.base.Loading
import com.huzzoid.konturexercise.domain.pojo.Contact

data class DetailsState(
    val initialId: String,
    val loading: Loading = Loading.FULL,
    val contact: Contact? = null
)