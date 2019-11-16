package com.linkplay.googleapi.network

import com.linkplay.googleapi.bean.Album
import com.linkplay.googleapi.bean.CreateAlbumRequestBody
import com.linkplay.googleapi.bean.ListAlbums
import io.reactivex.Observable
import retrofit2.http.*

interface AlbumAPI{

    @POST("v1/albums")
    fun createAlbum(@HeaderMap headers: Map<String, String>, @Body album: CreateAlbumRequestBody): Observable<Album>

    @GET("v1/albums")
    fun listAlbums(
            @HeaderMap headers: Map<String, String>,
            @Query("pageSize") pageSize: Int = 20,
            @Query("pageToken") pageToken: String = "",
            @Query("excludeNonAppCreatedData") excludeNonAppCreatedData: Boolean = false): Observable<ListAlbums>
}