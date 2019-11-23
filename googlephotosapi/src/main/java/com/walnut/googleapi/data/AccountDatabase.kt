package com.walnut.googleapi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Account::class, AccountRelationship::class], version = 1)
abstract class AccountDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getInstance(context: Context): AccountDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        AccountDatabase::class.java, "internal.db")
                        .build()
    }

    abstract fun accountDao(): AccountDao
    abstract fun accountRelationshipDao(): AccountRelationshipDao
}