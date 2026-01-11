package com.example.fitgod.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "istilah_gym",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id_user"],
            childColumns = ["id_user"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_user"])]
)
data class IstilahGymEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_istilah")
    val idIstilah: Int = 0,

    @ColumnInfo(name = "nama_istilah")
    val namaIstilah: String,

    @ColumnInfo(name = "kategori")
    val kategori: String,

    @ColumnInfo(name = "deskripsi")
    val deskripsi: String,

    @ColumnInfo(name = "id_user")
    val idUser: Int
)
