package com.azhar.banksampah.database.dao

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azhar.banksampah.model.ModelDatabase

/**
 * Created by Azhar Rivaldi on 6-12-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM tbl_banksampah")
    fun getAll(): LiveData<List<ModelDatabase>>

    @Query("SELECT SUM(harga) FROM tbl_banksampah")
    fun getSaldo(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(modelDatabases: ModelDatabase)

    @Query("DELETE FROM tbl_banksampah WHERE uid= :uid")
    fun deleteSingleData(uid: Int)

}