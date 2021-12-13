package com.azhar.banksampah.database

import android.content.Context
import androidx.room.Room

/**
 * Created by Azhar Rivaldi on 6-12-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/AzharRivaldi
 * Twitter : https://twitter.com/azharrvldi_
 * Instagram : https://www.instagram.com/azhardvls_
 * LinkedIn : https://www.linkedin.com/in/azhar-rivaldi
 */

class DatabaseClient private constructor(context: Context) {

    var appDatabase: AppDatabase

    companion object {
        private var mInstance: DatabaseClient? = null
        
        fun getInstance(context: Context): DatabaseClient? {
            if (mInstance == null) {
                mInstance = DatabaseClient(context)
            }
            return mInstance
        }
    }

    init {
        appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "banksampah_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}