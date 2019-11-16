package com.linkplay.googleapi.bean


data class Token(
        val scope: String? = null,
        val expires_in: Int? = null,
        val id_token: String? = null,
        val token_type: String? = null,
        val access_token: String? = null,
        var refresh_token: String? = null,
        // 请求错误时
        val error: String? = null,
        val error_description: String? = null
) : BaseResponse

