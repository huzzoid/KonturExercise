package com.huzzoid.konturexercise.data.net

import com.google.gson.annotations.SerializedName

data class ContactDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("biography")
    val biography: String,
    @SerializedName("temperament")
    val temperament: String,
    @SerializedName("educationPeriod")
    val educationPeriod: EducationPeriod
) {

    data class EducationPeriod(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String
    )
}