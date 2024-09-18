package com.abz.agency.testtask.core.api

sealed class Resource<out T> {
    data class Success<out T>(val data: T?) : Resource<T>()
    data class Failure<out R>(val errorType: R) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}