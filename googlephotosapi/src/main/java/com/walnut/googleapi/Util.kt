package com.walnut.googleapi

import com.walnut.googleapi.bean.BaseResponse
import com.walnut.googleapi.bean.Error
import com.google.gson.Gson
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.net.ssl.SSLHandshakeException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter


internal fun <T : Any> KClass<T>.createResponse(error: Error): T {
    var errorParameter: KParameter? = null
    val noArgConstructor = constructors.find { it ->
        errorParameter = it.parameters.find { it.type.classifier == Error::class }
        errorParameter != null
    }
    noArgConstructor
            ?: throw IllegalArgumentException("Class must have a constructor with an Error parameter")

    return noArgConstructor.callBy(mapOf(errorParameter!! to error))
}

internal inline fun <reified T : BaseResponse> catchError(e: Throwable): T? {
    try {
        return when (e) {
            is HttpException -> {
                val string = e.response()?.errorBody()?.string()
                string?.let { Gson().fromJson(it, T::class.java) }
            }
            is SSLHandshakeException -> T::class.createResponse(Error(code = BaseResponse.SSL_EXCEPTION, message = e.message))
            else -> T::class.createResponse(Error(code = BaseResponse.UNKNOWN_EXCEPTION, message = e.message))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return T::class.createResponse(Error(code = BaseResponse.UNKNOWN_EXCEPTION, message = e.message))
}

internal inline fun <reified T : BaseResponse> Observable<T>.applySchedulers(): Observable<T> = subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturn { e -> catchError(e) }

internal inline fun <reified T : BaseResponse> Observable<T>.applySchedulersNotMain(): Observable<T> = subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .onErrorReturn { e -> catchError(e) }

internal inline fun <reified T> Maybe<T>.applySchedulers(): Maybe<T> = subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())