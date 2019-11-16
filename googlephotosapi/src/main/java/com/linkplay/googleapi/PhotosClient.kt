package com.linkplay.googleapi

import android.annotation.SuppressLint
import android.util.Log
import com.linkplay.googleapi.bean.Album
import com.linkplay.googleapi.bean.ListAlbums
import com.linkplay.googleapi.bean.createAlbumRequestBody
import com.linkplay.googleapi.network.AlbumAPI
import com.linkplay.googleapi.network.PhotosClient

@SuppressLint("CheckResult")
internal object PhotosClient {

    private const val TAG = "PhotosClient"

    private val headerMap: Map<String, String>

    init {
        headerMap = mutableMapOf("X-Goog-AuthUser" to "0")
        GoogleClient.get().tokenClient.getToken().observeForever {
            headerMap["Authorization"] = "Bearer ${it.access_token}"
        }
    }

    fun createAlbum(title: String) {

        if (!headerMap.containsKey("Authorization") || headerMap["Authorization"].isNullOrEmpty()) {
            Log.e(TAG, "token is null")
            return
        }

        Log.i(TAG, "createAlbum: $title")

        val observable = PhotosClient.create(AlbumAPI::class.java)
                .createAlbum(headerMap, createAlbumRequestBody(title))

        observable.applySchedulers()
                .subscribe { album: Album ->
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