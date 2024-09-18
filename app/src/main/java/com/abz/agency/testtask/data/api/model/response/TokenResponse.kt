package com.abz.agency.testtask.data.api.model.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String
)