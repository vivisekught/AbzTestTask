package com.abz.agency.testtask.core.api

sealed class UploadUserErrorType {
    data object UserAlreadyExists : UploadUserErrorType()
    data object OtherError : UploadUserErrorType()
}