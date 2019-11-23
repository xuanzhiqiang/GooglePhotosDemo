package com.walnut.googleapi.data

import androidx.room.*

@Dao
interface AccountRelationshipDao {

    @Delete
    fun delete(accountRelationship: AccountRelationship)
}