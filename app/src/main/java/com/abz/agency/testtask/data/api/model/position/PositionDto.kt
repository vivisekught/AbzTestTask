package com.abz.agency.testtask.data.api.model.position

import com.google.gson.annotations.SerializedName

data class PositionsDto(
    val positions: List<PositionDto>,
)

data class PositionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)