package com.abz.agency.testtask.data.api.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("position") val position: String,
    @SerializedName("position_id") val positionId: Int,
    @SerializedName("registration_timestamp") val registrationTimestamp: Long,
    @SerializedName("photo") val photoPath: String,
)