package com.abz.agency.testtask.domain.usecases

import com.abz.agency.testtask.domain.UsersApiRepository
import javax.inject.Inject

class GetPositionsUseCase @Inject constructor(
    private val apiRepository: UsersApiRepository
) {

    suspend operator fun invoke() = apiRepository.getPositions()
}