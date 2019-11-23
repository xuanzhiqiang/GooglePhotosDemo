package com.walnut.googleapi.network

import com.walnut.googleapi.bean.Token
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenPAI {

    companion object {
        const val BASE_URL = "https://www.googleapis.com"
    }

    @POST("oauth2/v4/token")
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun requestToken(
            @Field("code") code: String,
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType: String = "authorization_code"): Observable<Token>


    @POST("oauth2/v4/token")
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun refreshToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("refresh_token") refreshToken: String,
            @Field("grant_type") grantType: String = "refresh_token"): Observable<Token>
}