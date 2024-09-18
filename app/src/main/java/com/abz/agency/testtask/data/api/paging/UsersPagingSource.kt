package com.abz.agency.testtask.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abz.agency.testtask.core.api.Constants.DEFAULT_PAGE
import com.abz.agency.testtask.core.api.Constants.PAGE_SIZE
import com.abz.agency.testtask.domain.UsersApiRepository
import com.abz.agency.testtask.domain.entity.UserEntity
import javax.inject.Inject

class UsersPagingSource @Inject constructor(
    private val usersApiRepository: UsersApiRepository,
) : PagingSource<Int, UserEntity>() {

    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, UserEntity> {
        return try {
            // Get the current page to load. If the key is null, load the default page.
            val currentPage = params.key ?: DEFAULT_PAGE

            // Fetch the users from the API for the given page and page size
            val users = usersApiRepository.getUsers(page = currentPage, size = PAGE_SIZE)
            LoadResult.Page(
                data = users,
                prevKey = if (currentPage <= 1) null else currentPage - 1,
                nextKey = if (users.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition
    }
}