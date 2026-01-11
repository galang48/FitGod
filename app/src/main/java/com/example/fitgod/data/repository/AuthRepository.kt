package com.example.fitgod.data.repository

import com.example.fitgod.data.remote.api.FitGodApi
import com.example.fitgod.data.remote.model.LoginRequest
import com.example.fitgod.data.remote.model.RegisterRequest
import com.example.fitgod.data.remote.model.UserDto

class AuthRepository {

    // Ambil Retrofit service dari objek FitGodApi
    private val api = FitGodApi.service

    suspend fun register(username: String, password: String): Result<UserDto> {
        return try {
            val body = RegisterRequest(username = username, password = password)
            val response = api.register(body)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<UserDto> {
        return try {
            val body = LoginRequest(username = username, password = password)
            val response = api.login(body)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
