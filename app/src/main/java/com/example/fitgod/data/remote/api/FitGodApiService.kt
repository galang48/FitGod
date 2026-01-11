package com.example.fitgod.data.remote.api

import com.example.fitgod.data.remote.model.ApiResponse
import com.example.fitgod.data.remote.model.UserDto
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.data.remote.model.RegisterRequest
import com.example.fitgod.data.remote.model.LoginRequest
import com.example.fitgod.data.remote.model.IstilahCreateRequest
import com.example.fitgod.data.remote.model.IstilahUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FitGodApiService {

    // ========================= AUTH =========================

    // POST http://10.0.2.2/fitgod_api/auth_register.php
    @POST("auth_register.php")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<UserDto>

    // POST http://10.0.2.2/fitgod_api/auth_login.php
    @POST("auth_login.php")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<UserDto>

    // ====================== ISTILAH GYM =====================

    // GET http://10.0.2.2/fitgod_api/get_istilah.php?userId=1&search=... (search optional)
    @GET("get_istilah.php")
    suspend fun getIstilah(
        @Query("userId") userId: Int,
        @Query("search") search: String? = null
    ): ApiResponse<List<IstilahDto>>

    // POST http://10.0.2.2/fitgod_api/create_istilah.php
    @POST("create_istilah.php")
    suspend fun addIstilah(
        @Body body: IstilahCreateRequest
    ): ApiResponse<IstilahDto>

    // PUT http://10.0.2.2/fitgod_api/update_istilah.php?id=2
    @PUT("update_istilah.php")
    suspend fun updateIstilah(
        @Query("id") id: Int,
        @Body body: IstilahUpdateRequest
    ): ApiResponse<Unit>

    // DELETE http://10.0.2.2/fitgod_api/delete_istilah.php?id=2
    @DELETE("delete_istilah.php")
    suspend fun deleteIstilah(
        @Query("id") id: Int
    ): ApiResponse<Unit>
}
