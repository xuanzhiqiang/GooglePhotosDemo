package com.linkplay.googleapi

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.linkplay.googleapi.bean.Token
import com.linkplay.googleapi.network.TokenPAI
import com.linkplay.googleapi.network.TokenRetrofitClient

@SuppressLint("CheckResult")
internal class TokenClient(val application: Application) {

    companion object {
        private const val TAG = "TokenClient"
        private const val clientId = "520003967142-6kbrs0vdfp806r01van7pdu8gb87f7hg.apps.googleusercontent.com"
        private const val clientSecret = "kXXXqDmbekY8QRfD4sYamGQD"
        const val PREFERENCES = "config_data"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    fun noNeedSignIn() = token.value != null && token.value?.refresh_token != null

    private val token: MutableLiveData<Token> by lazy {
        val access = application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(ACCESS_TOKEN, null)
        val refresh = application.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(REFRESH_TOKEN, null)
        val mutableLiveData = MutableLiveData<Token>()
        access?.let {
            mutableLiveData.value = Token(access_token = access, refresh_token = refresh)
        }
        return@lazy mutableLiveData
    }

    private fun saveToken(t: Token) {
        token.postValue(t)
        val sp = application.getSharedPreferences(GoogleClient.PREFERENCES, Context.MODE_PRIVATE)
        val edit = sp.edit().putString(GoogleClient.ACCESS_TOKEN, t.access_token)
        t.refresh_token?.let {
            edit.putString(GoogleClient.REFRESH_TOKEN, t.refresh_token)
        }
        edit.apply()
    }

    fun getToken(): LiveData<Token> = token

    fun requestToken(code: String) {
        Log.i(TAG, "requestToken start: $code")
        val observable = TokenRetrofitClient
                .create(TokenPAI::class.java)
                .requestToken(
                        clientId = clientId,
                        clientSecret = clientSecret,
                        code = code)
        observable
                .applySchedulers()
                .subscribe { t: Token ->
                    t.error?.let {
                        Log.e(TAG, "error: $it")
                        Log.e(TAG, "error_description: ${t.error_description}")
                        return@subscribe
                    }
                    Log.i(TAG, "requestToken success: $t")
                    saveToken(t)
                }
    }

    fun refreshToken() {
        token.value?.refresh_token?.let { refreshToken ->
            Log.i(TAG, "refreshToken start: $refreshToken")

            val observable = TokenRetrofitClient
                    .create(TokenPAI::class.java)
                    .refreshToken(
                            clientId = clientId,
                            clientSecret = clientSecret,
                            refreshToken = refreshToken)
            observable
                    .applySchedulers()
                    .subscribe { t: Token ->
                        t.error?.let {
                            Log.e(TAG, "error: $it")
                            Log.e(TAG, "error_description: ${t.error_description}")
                            return@subscribe
                        }
                        if (t.refresh_token == null) {
                            t.refresh_token = refreshToken
                        }
                        Log.i(TAG, "requestToken success: $t")
                        saveToken(t)
                    }
            return
        }
        Log.i(TAG, "refreshToken error: ${token.value}")
    }

}

