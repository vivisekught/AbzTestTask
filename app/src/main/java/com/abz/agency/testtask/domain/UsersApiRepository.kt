package com.abz.agency.testtask.domain

import com.abz.agency.testtask.core.api.Resource
import com.abz.agency.testtask.data.api.model.response.UserUploadResponse
import com.abz.agency.testtask.domain.entity.PositionEntity
import com.abz.agency.testtask.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UsersApiRepository {

    suspend fun getUsers(page: Int, size: Int): List<UserEntity>

    suspend fun getPositions(): List<PositionEntity>

    suspend fun postUser(userEntity: UserEntity): Flow<Resource<UserUploadResponse>>
}