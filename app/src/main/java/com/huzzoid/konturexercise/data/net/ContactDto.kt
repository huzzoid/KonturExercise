package com.huzzoid.konturexercise.data.net

import com.google.gson.annotations.SerializedName
import com.huzzoid.konturexercise.data.db.ContactEntity

data class ContactDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("height")
    val height: Float,
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

fun ContactDto.toEntity() = ContactEntity(
    id,
    name,
    phone,
    height,
    biography,
    temperament,
    educationPeriod.start,
    educationPeriod.end
)