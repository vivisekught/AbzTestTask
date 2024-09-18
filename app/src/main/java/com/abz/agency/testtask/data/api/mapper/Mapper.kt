package com.abz.agency.testtask.data.api.mapper

import com.abz.agency.testtask.data.api.model.position.PositionDto
import com.abz.agency.testtask.data.api.model.user.UserDto
import com.abz.agency.testtask.domain.entity.PositionEntity
import com.abz.agency.testtask.domain.entity.UserEntity

fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone,
    position = position,
    positionId = positionId,
    registrationTimestamp = registrationTimestamp,
    photoPath = photoPath
)

fun PositionDto.toEntity() = PositionEntity(
    id = id,
    name = name
)