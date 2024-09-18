package com.abz.agency.testtask.data.api.model.user

import com.google.gson.annotations.SerializedName

data class PageDto<T>(
    @SerializedName("users") val users: List<T>
)