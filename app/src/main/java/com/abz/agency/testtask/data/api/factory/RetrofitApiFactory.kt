package com.abz.agency.testtask.data.api.factory

import com.abz.agency.testtask.core.api.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiFactory {

    fun <T> createInstance(clazz: Class<T>): T {

        // Retrieve the @ApiUrl annotation from the provided class if it exists.
        val apiUrlAnnotation = clazz.annotations.find { it is ApiUrl } as ApiUrl?

        // If the annotation is found, use its URL; otherwise, use the default MOVIES_API_URL.
        val url = apiUrlAnnotation?.url ?: Constants.URL

        // Build and return a Retrofit instance for the provided class, using the URL.
        return retrofit(url).create(clazz)
    }

    private fun retrofit(apiUrl: String) = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
}