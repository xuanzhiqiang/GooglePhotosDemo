package com.walnut.googleapi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
        @PrimaryKey(autoGenerate = false)
        val id: String,
        @ColumnInfo(name = "email") val email: String?,
        @ColumnInfo(name = "given_name") val givenName: String?,
        @ColumnInfo(name = "family_name") val familyName: String?,
        @ColumnInfo(name = "display_name") val displayName: String?,
        @ColumnInfo(name = "photo_url") val photoUrl: String?,
        @ColumnInfo(name = "refresh_token") var refreshToken: String? = null,
        @ColumnInfo(name = "access_token") var accessToken: String? = null,
        @ColumnInfo(name = "expires_in") var expiresIn: Long = 0,

        @ColumnInfo(name = "use") val use: String? = "photo"
)




