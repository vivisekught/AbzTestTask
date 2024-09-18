package com.abz.agency.testtask.domain.entity

data class UserEntity(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val position: String = "",
    val positionId: Int = 0,
    val registrationTimestamp: Long = 0,
    val photoPath: String = "",
)