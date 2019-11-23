package com.walnut.googleapi

import android.annotation.SuppressLint
import android.util.Log
import com.walnut.googleapi.bean.Album
import com.walnut.googleapi.bean.ListAlbums
import com.walnut.googleapi.bean.createAlbumRequestBody
import com.walnut.googleapi.network.*
import com.walnut.googleapi.network.PhotosClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
internal object PhotosClient {

    private const val TAG = "PhotosClient"

    private val headerMap: MutableMap<String, String> = mutableMapOf("X-Goog-AuthUser" to "0")
    private var accessToken: String = ""

    fun createAlbum(title: String) {

        val photosAccount = AccountManager.currentPhotosAccount
        if (photosAccount == null || photosAccount.accessToken.isNullOrEmpty()) {
            Log.e(TAG, "token is null")
            return
        }
        GoogleClient.get().accountDatabase
                .accountDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe {
                    Log.i(TAG, "subscribe currentThread: ${Thread.currentThread().name}")
                    it.forEach { account ->
                        Log.i(TAG, "id: ${account.id}")
                        Log.i(TAG, "displayName: ${account.displayName}")
                    }
                }

        GoogleClient.get()
                .accountDatabase.accountDao()
                .findByID(photosAccount.id)
                .subscribeOn(Schedulers.io())
                .flatMapObservable {
                    Log.i(TAG, "flatMapObservable refreshToken currentThread: ${Thread.currentThread().name}")
                    GoogleClient.get().tokenClient.refreshToken(it)
                }
                .flatMap { accessToken ->
                    Log.i(TAG, "flatMapObservable currentThread: ${Thread.currentThread().name}")

                    Log.i(TAG, "createAlbum: $title")
                    Log.i(TAG, "accessToken: $accessToken")
                    headerMap["Authorization"] = "Bearer $accessToken"
                    PhotosClient.create(AlbumAPI::class.java).createAlbum(headerMap, createAlbumRequestBody(title))
                }
                .applySchedulers()
                .subscribe { album: Album ->
                    Log.i(TAG, "subscribe currentThread: ${Thread.currentThread().name}")
                    Log.i(TAG, "createAlbum: $album")
                }

    }

    fun listAlbums() {
        if (!headerMap.containsKey("Authorization") || headerMap["Authorization"].isNullOrEmpty()) {
            Log.e(TAG, "token is null")
            return
        }
        val observable = PhotosClient.create(AlbumAPI::class.java).listAlbums(headerMap)
        observable.applySchedulers()
                .subscribe { albums: ListAlbums ->
                    Log.i(TAG, "listAlbums: $albums")
                }
    }

}