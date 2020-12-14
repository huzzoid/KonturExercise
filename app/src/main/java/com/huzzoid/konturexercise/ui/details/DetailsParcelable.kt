package com.huzzoid.konturexercise.ui.details

import android.os.Parcelable
import com.huzzoid.konturexercise.domain.details.DetailsState
import kotlinx.parcelize.Parcelize


@Parcelize
data class DetailsParcelable(val initialId: String) : Parcelable

fun DetailsParcelable.toState() = DetailsState(initialId)

fun DetailsState.toParcelable() = DetailsParcelable(initialId)