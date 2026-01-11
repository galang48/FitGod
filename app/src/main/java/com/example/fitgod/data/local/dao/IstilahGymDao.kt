package com.example.fitgod.data.local.dao

import androidx.room.*
import com.example.fitgod.data.local.entity.IstilahGymEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IstilahGymDao {

    @Query("SELECT * FROM istilah_gym ORDER BY nama_istilah ASC")
    fun getAllIstilah(): Flow<List<IstilahGymEntity>>

    @Query(
        "SELECT * FROM istilah_gym " +
                "WHERE nama_istilah LIKE '%' || :keyword || '%' " +
                "ORDER BY nama_istilah ASC"
    )
    fun searchIstilah(keyword: String): Flow<List<IstilahGymEntity>>

    @Query("SELECT * FROM istilah_gym WHERE id_istilah = :id LIMIT 1")
    suspend fun getIstilahById(id: Int): IstilahGymEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIstilah(istilah: IstilahGymEntity): Long

    @Update
    suspend fun updateIstilah(istilah: IstilahGymEntity)

    @Delete
    suspend fun deleteIstilah(istilah: IstilahGymEntity)
}
