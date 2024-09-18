package com.abz.agency.testtask.data.api.service

import com.abz.agency.testtask.core.api.Constants
import com.abz.agency.testtask.core.api.Constants.PAGE_SIZE
import com.abz.agency.testtask.data.api.factory.ApiUrl
import com.abz.agency.testtask.data.api.model.position.PositionsDto
import com.abz.agency.testtask.data.api.model.response.TokenResponse
import com.abz.agency.testtask.data.api.model.response.UserUploadResponse
import com.abz.agency.testtask.data.api.model.user.PageDto
import com.abz.agency.testtask.data.api.model.user.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

@ApiUrl(Constants.URL)
interface UsersService {

    @GET("api/v1/users")
    suspend fun get(
        @Query(QUERY_PARAM_PAGE) page: Int,
        @Query(QUERY_PARAM_COUNT) size: Int = PAGE_SIZE,
    ): Response<PageDto<UserDto>>

    @GET("api/v1/users/{id}")
    suspend fun get(@Path(PATH_ID) id: Int): Response<UserDto>

    @GET("api/v1/positions")
    suspend fun getPositions(): Response<PositionsDto>

    @Multipart
    @POST("api/v1/users")
    suspend fun post(
        @Header("Token") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("position_id") positionId: RequestBody,
        @Part photo: MultipartBody.Part,
    ): UserUploadResponse

    @GET("api/v1/token")
    suspend fun getRefreshToken(): Response<TokenResponse>

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val QUERY_PARAM_COUNT = "count"
        private const val PATH_ID = "id"
    }
}