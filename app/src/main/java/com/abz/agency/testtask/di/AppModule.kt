package com.abz.agency.testtask.di

import com.abz.agency.testtask.data.api.factory.RetrofitApiFactory
import com.abz.agency.testtask.data.api.UsersApiRepositoryImpl
import com.abz.agency.testtask.data.api.service.UsersService
import com.abz.agency.testtask.data.connection.NetworkConnectivityRepositoryImpl
import com.abz.agency.testtask.domain.NetworkConnectivityRepository
import com.abz.agency.testtask.domain.UsersApiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindUsersApiRepository(impl: UsersApiRepositoryImpl): UsersApiRepository

    @Binds
    fun bindNetworkConnectivityRepository(impl: NetworkConnectivityRepositoryImpl): NetworkConnectivityRepository

    companion object {
        @Provides
        @Singleton
        fun provideElevationApiInstance() =
            RetrofitApiFactory().createInstance(UsersService::class.java)
    }
}