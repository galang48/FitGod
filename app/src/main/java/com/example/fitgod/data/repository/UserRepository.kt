package com.example.fitgod.data.repository

import com.example.fitgod.data.local.dao.UserDao
import com.example.fitgod.data.local.entity.UserEntity
import com.example.fitgod.data.remote.model.UserDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository(
    private val userDao: UserDao
) {
    private val dateFormat =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // Simpan / update user dari API ke Room
    suspend fun cacheUserFromApi(dto: UserDto): UserEntity {
        val entity = UserEntity(
            idUser = dto.idUser,
            username = dto.username,
            password = "", // password tidak perlu disimpan di lokal
            tglDaftar = dto.tglDaftar ?: dateFormat.format(Date())
        )
        userDao.insertUser(entity)   // sekarang REPLACE, tidak error lagi
        return entity
    }

    suspend fun getUserById(id: Int): UserEntity? =
        userDao.getUserById(id)

    suspend fun clearUsers() =
        userDao.clearUsers()
}
