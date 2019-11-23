package com.walnut.googleapi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Account_relationship")
data class AccountRelationship(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "device_uuid") val deviceUUID: String,
        @ColumnInfo(name = "lenovo_account_id") val lenovoAccountId: String,
        @ColumnInfo(name = "google_account_id") val googleAccountId: String
)