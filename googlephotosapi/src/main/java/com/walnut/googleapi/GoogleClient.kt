package com.walnut.googleapi

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.walnut.googleapi.data.Account
import com.walnut.googleapi.data.AccountDatabase
import com.walnut.googleapi.network.AccountManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GoogleClient private constructor(val application: Application) {

    companion object {
        const val TAG = "GoogleClient"

        @Volatile
        private var INSTANCE: GoogleClient? = null

        fun init(application: Application): GoogleClient =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: GoogleClient(application).also { INSTANCE = it }
                }

        fun get() = INSTANCE
                ?: throw RuntimeException("Please initialize GoogleClient first.")
    }

//    val accountDatabase by lazy {
//        AccountDatabase.getInstance(application)
//    }

    val accountDatabase = AccountDatabase.getInstance(application)

    internal val tokenClient = TokenClient(application)

    @SuppressLint("CheckResult")
    fun changePhotosAccount(account: Account) {
        AccountManager.currentPhotosAccount = account
        accountDatabase.accountDao()
                .insert(account)
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe {
                    Log.i(TAG, "insert ${account.email} success")
                }
    }

    @SuppressLint("CheckResult")
    fun changeCalendarAccount(account: Account) {
        AccountManager.currentCalendarAccount = account
        accountDatabase.accountDao()
                .insert(account)
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe {
                    Log.i(TAG, "insert ${account.email} success")
                }
    }

    /**
     * 只需要在登录成功的时候请求【access_token、refresh_token】一次就可以
     *
     * 如果用户清除了应用缓存数据，尝试使用Google静默登录， 得到serverAuthCode再次调用该方法
     */
    fun requestToken(serverAuthCode: String) {
        tokenClient.requestToken(serverAuthCode, AccountManager.currentPhotosAccount!!)
    }

    fun createAlbum(title: String) {
        PhotosClient.createAlbum(title)
    }

    fun listAlbums() {
        PhotosClient.listAlbums()
    }

}