package com.abz.agency.testtask.domain

import com.abz.agency.testtask.core.ConnectionState
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityRepository {

    val networkStatus: Flow<ConnectionState>
}