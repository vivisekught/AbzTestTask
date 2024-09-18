package com.abz.agency.testtask.data.api

import android.content.Context
import android.net.Uri
import com.abz.agency.testtask.core.api.Constants.PAGE_SIZE
import com.abz.agency.testtask.core.FileUtils.getRealPathFromURI
import com.abz.agency.testtask.core.FileUtils.isPhotoFromGallery
import com.abz.agency.testtask.core.api.Resource
import com.abz.agency.testtask.core.api.UploadUserErrorType
import com.abz.agency.testtask.data.api.mapper.toEntity
import com.abz.agency.testtask.data.api.model.response.UserUploadResponse
import com.abz.agency.testtask.data.api.service.UsersService
import com.abz.agency.testtask.domain.UsersApiRepository
import com.abz.agency.testtask.domain.entity.PositionEntity
import com.abz.agency.testtask.domain.entity.UserEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject


class UsersApiRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: UsersService,
) : UsersApiRepository {

    override suspend fun getUsers(page: Int, size: Int): List<UserEntity> {
        val response = service.get(page = page, size = PAGE_SIZE)
        return try {
            response.body()?.users?.map { dto -> dto.toEntity() } ?: listOf()
        } catch (e: HttpException) {
            listOf()
        }
    }

    override suspend fun getPositions(): List<PositionEntity> {
        val response = service.getPositions()
        return try {
            response.body()?.positions?.map { positionDto -> positionDto.toEntity() } ?: listOf()
        } catch (e: HttpException) {
            listOf()
        }
    }

    override suspend fun postUser(userEntity: UserEntity): Flow<Resource<UserUploadResponse>> {
        return flow {
            try {
                emit(Resource.Loading)
                val tokenResponse = service.getRefreshToken()
                if (!tokenResponse.isSuccessful || tokenResponse.body()?.token == null) {
                    emit(Resource.Failure(UploadUserErrorType.OtherError))
                    return@flow
                }
                val token = tokenResponse.body()?.token ?: return@flow
                // Create RequestBody instances for the fields
                val name = userEntity.name.toRequestBody(TEXT_PLANE_MIME_TYPE.toMediaTypeOrNull())
                val email = userEntity.email.toRequestBody(TEXT_PLANE_MIME_TYPE.toMediaTypeOrNull())
                val phone = userEntity.phone.toRequestBody(TEXT_PLANE_MIME_TYPE.toMediaTypeOrNull())
                val positionId =
                    userEntity.positionId.toString()
                        .toRequestBody(TEXT_PLANE_MIME_TYPE.toMediaTypeOrNull())

                // Create MultipartBody.Part for the photo
                val uri = Uri.parse(userEntity.photoPath)
                val fileName = userEntity.photoPath.substringAfterLast("/")
                val photoPart = createMultipartBody(uri, fileName)

                // Make the POST request and get response
                val response = service.post(
                    token = token,
                    name = name,
                    email = email,
                    phone = phone,
                    positionId = positionId,
                    photo = photoPart
                )
                if (response.success) {
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Failure(UploadUserErrorType.OtherError))
                }
            } catch (e: IOException) {
                emit(Resource.Failure(UploadUserErrorType.OtherError))
            } catch (e: HttpException) {
                // User already exists
                if (e.code() == 409) {
                    emit(Resource.Failure(UploadUserErrorType.UserAlreadyExists))
                } else {
                    emit(Resource.Failure(UploadUserErrorType.OtherError))
                }
            }
        }
    }

    // Create MultipartBody.Part for the photo from Uri
    private fun createMultipartBody(uri: Uri, fileName: String): MultipartBody.Part {
        // Check if the Uri is from the gallery or from the camera
        val file = if (isPhotoFromGallery(context, uri)) {
            // Get path for photo from gallery
            val galleryPhotoPath = getRealPathFromURI(uri, context).toString()
            File(galleryPhotoPath)
        } else {
            // Get path for photo from camera
            // externalCacheDir is used, see in xml/file_paths.xml
            File(context.externalCacheDir, fileName)
        }
        // Create MultipartBody.Part for the photo
        val requestFile = file.asRequestBody(IMAGE_MIME_TYPE.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(FORM_DATA_NAME, file.name, requestFile)
    }

    companion object {
        private const val IMAGE_MIME_TYPE = "image/jpeg"
        private const val FORM_DATA_NAME = "photo"
        private const val TEXT_PLANE_MIME_TYPE = "text/plain"
    }
}