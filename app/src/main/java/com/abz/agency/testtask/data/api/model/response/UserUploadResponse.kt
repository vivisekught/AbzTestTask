package com.abz.agency.testtask.data.api.model.response

import com.google.gson.annotations.SerializedName

data class UserUploadResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("message") val message: String = ""
)