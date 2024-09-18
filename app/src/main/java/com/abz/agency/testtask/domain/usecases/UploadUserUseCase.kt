package com.abz.agency.testtask.domain.usecases

import com.abz.agency.testtask.domain.UsersApiRepository
import com.abz.agency.testtask.domain.entity.UserEntity
import javax.inject.Inject

class UploadUserUseCase @Inject constructor(
    private val usersApiRepository: UsersApiRepository
) {

    suspend operator fun invoke(userEntity: UserEntity) = usersApiRepository.postUser(userEntity)
}