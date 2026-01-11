package com.example.fitgod.data.remote.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

object FitGodApi {

    // Emulator Android -> localhost XAMPP di laptop
    private const val BASE_URL = "http://10.0.2.2/fitgod_api/"

    // Konfigurasi JSON
    private val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // HTTP client + logging
    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Retrofit service
    val service: FitGodApiService by lazy {
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(FitGodApiService::class.java)
    }
}



