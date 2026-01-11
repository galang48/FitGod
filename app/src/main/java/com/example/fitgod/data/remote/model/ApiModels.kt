package com.example.fitgod.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

/**
 * Data user yang dikirim balik dari API PHP.
 * Contoh JSON (kurang lebih):
 * {
 *   "id_user": 1,
 *   "username": "baginda",
 *   "tgl_daftar": "2025-12-29 20:15:00"
 * }
 */
@Serializable
data class UserDto(
    @SerialName("id_user")
    val idUser: Int,

    @SerialName("username")
    val username: String,

    // Di JSON namanya "tgl_daftar", di Kotlin kita pakai camelCase "tglDaftar"
    @SerialName("tgl_daftar")
    val tglDaftar: String? = null
)

/**
 * Data istilah gym dari API.
 * Contoh JSON:
 * {
 *   "id_istilah": 2,
 *   "nama_istilah": "Barbel",
 *   "kategori": "Alat Gym",
 *   "deskripsi": "Latihan Melatih Otot Dada dan Otot Lengan",
 *   "id_user": 1
 * }
 */
@Serializable
data class IstilahDto(
    @SerialName("id_istilah")
    val idIstilah: Int,

    @SerialName("nama_istilah")
    val namaIstilah: String,

    val kategori: String,
    val deskripsi: String,

    @SerialName("id_user")
    val idUser: Int
)
