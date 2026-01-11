package com.example.fitgod.data.repository

import com.example.fitgod.data.remote.api.FitGodApi
import com.example.fitgod.data.remote.model.IstilahCreateRequest
import com.example.fitgod.data.remote.model.IstilahDto
import com.example.fitgod.data.remote.model.IstilahUpdateRequest

class IstilahGymRepository {

    private val api = FitGodApi.service

    suspend fun getIstilahGym(
        userId: Int,
        search: String? = null
    ): Result<List<IstilahDto>> {
        return try {
            val response = api.getIstilah(userId = userId, search = search)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun tambahIstilah(
        userId: Int,
        namaIstilah: String,
        kategori: String,
        deskripsi: String
    ): Result<IstilahDto> {
        return try {
            val body = IstilahCreateRequest(
                nama_istilah = namaIstilah,
                kategori = kategori,
                deskripsi = deskripsi,
                id_user = userId
            )
            val response = api.addIstilah(body)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateIstilah(
        idIstilah: Int,
        namaIstilah: String,
        kategori: String,
        deskripsi: String
    ): Result<Unit> {
        return try {
            val body = IstilahUpdateRequest(
                nama_istilah = namaIstilah,
                kategori = kategori,
                deskripsi = deskripsi
            )
            val response = api.updateIstilah(idIstilah, body)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hapusIstilah(idIstilah: Int): Result<Unit> {
        return try {
            val response = api.deleteIstilah(idIstilah)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
