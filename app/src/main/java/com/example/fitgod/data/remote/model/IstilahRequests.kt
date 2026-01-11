package com.example.fitgod.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class IstilahCreateRequest(
    val nama_istilah: String,
    val kategori: String,
    val deskripsi: String,
    val id_user: Int
)

@Serializable
data class IstilahUpdateRequest(
    val nama_istilah: String,
    val kategori: String,
    val deskripsi: String
)
