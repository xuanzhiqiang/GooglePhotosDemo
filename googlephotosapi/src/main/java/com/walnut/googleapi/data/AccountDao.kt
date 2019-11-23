package com.walnut.googleapi.data

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe


@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAll(): Maybe<List<Account>>

    @Query("SELECT * FROM account WHERE use LIKE :use")
    fun loadAllByUse(use: String): Maybe<List<Account>>

    @Query("SELECT * FROM account WHERE id LIKE :id LIMIT 1")
    fun findByID(id: String): Maybe<Account>

    @Query("SELECT * FROM account WHERE access_token LIKE :access_token LIMIT 1")
    fun findByAccessToken(access_token: String): Maybe<Account>

//    @Query()
//    fun exist(id: String): Boolean

    @Insert
    fun insertAll(vararg accounts: Account): Completable

    @Insert
    fun insert(account: Account): Completable

    @Update
    fun update(account: Account): Completable

    @Delete
    fun delete(account: Account): Completable
}