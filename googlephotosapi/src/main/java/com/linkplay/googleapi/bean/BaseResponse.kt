package com.linkplay.googleapi.bean


data class Error(
        val code: Int? = 0,
        val message: String? = null,
        val status: String? = null
)


interface BaseResponse{
    companion object{
        const val UNKNOWN_EXCEPTION = -1
        const val SSL_EXCEPTION = -2
    }
}