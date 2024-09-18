package com.abz.agency.testtask.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.abz.agency.testtask.data.api.paging.UsersPagingSource
import com.abz.agency.testtask.domain.entity.UserEntity
import com.abz.agency.testtask.core.api.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val usersPagingSource: UsersPagingSource,
) {
    operator fun invoke(scope: CoroutineScope): Flow<PagingData<UserEntity>> =
        Pager(PagingConfig(pageSize = Constants.PAGE_SIZE)) {
            usersPagingSource
        }.flow.cachedIn(scope)
}