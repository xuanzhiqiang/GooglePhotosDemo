package com.linkplay.googleapi

import android.app.Application

class GoogleClient private constructor(val application: Application) {

    companion object {
        const val TAG = "GoogleClient"
        const val PREFERENCES = "config_data"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"

        @Volatile
        private var INSTANCE: GoogleClient? = null

        @Synchronized
        operator fun invoke(application: Application): GoogleClient {
            if (INSTANCE == null) {
                INSTANCE = GoogleClient(application)
            }
            return INSTANCE as GoogleClient
        }

        internal fun get() = INSTANCE ?: throw RuntimeException("Please initialize GoogleClient first.")
    }

    internal val tokenClient = TokenClient(application)

    /**
     * 只需要在登录成功的时候请求【access_token、refresh_token】一次就可以
     *
     * 如果用户清除了应用缓存数据，尝试使用Google静默登录， 得到serverAuthCode再次调用该方法
     */
    fun requestToken(serverAuthCode: String){
        tokenClient.requestToken(serverAuthCode)
    }

    fun needSignIn() = !tokenClient.noNeedSignIn()

    fun refreshToken(){
        tokenClient.refreshToken()
    }

    fun createAlbum(title: String){
        PhotosClient.createAlbum(title)
    }

    fun listAlbums(){
        PhotosClient.listAlbums()
    }

}