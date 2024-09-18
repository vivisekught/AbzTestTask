package com.abz.agency.testtask.domain.usecases

import com.abz.agency.testtask.domain.NetworkConnectivityRepository
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(
    private val networkConnectivityRepository: NetworkConnectivityRepository
) {

    operator fun invoke() = networkConnectivityRepository.networkStatus
}