package com.walnut.googleapi

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.walnut.googleapi.bean.Token
import com.walnut.googleapi.data.Account
import com.walnut.googleapi.network.TokenPAI
import com.walnut.googleapi.network.TokenRetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
internal class TokenClient(val application: Application) {

    companion object {
        private const val TAG = "TokenClient"
        const val clientId = "520003967142-6kbrs0vdfp806r01van7pdu8gb87f7hg.apps.googleusercontent.com"
        const val clientSecret = "kXXXqDmbekY8QRfD4sYamGQD"
    }

    fun requestToken(code: String, account: Account) {
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
                    Log.i(TAG, "requestToken success.")
                    account.accessToken = t.access_token
                    account.refreshToken = t.refresh_token
                    account.expiresIn = System.currentTimeMillis() + t.expires_in?.toLong()!! * 1000

                    GoogleClient.get().accountDatabase
                            .accountDao()
                            .update(account)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                Log.i(GoogleClient.TAG, "update ${account.email} success")
                            }


                }
    }

    fun refreshToken(account: Account): Observable<String> {

        if (account.refreshToken.isNullOrEmpty()) {
            return Observable.create<String> {
                it.onNext("")
                it.onComplete()
            }
        }

        if (account.expiresIn > System.currentTimeMillis()) {
            return Observable.create<String> {
                it.onNext(account.accessToken!!)
                it.onComplete()
            }
        }

        Log.i(TAG, "refreshToken start: ${account.refreshToken}")
        val observable = TokenRetrofitClient
                .create(TokenPAI::class.java)
                .refreshToken(
                        clientId = clientId,
                        clientSecret = clientSecret,
                        refreshToken = account.refreshToken!!)

        return observable
                .applySchedulersNotMain()
                .map {

                    it.error?.let { error ->
                        Log.e(TAG, "Refresh token fail: $error")
                        Log.e(TAG, "error_description: ${it.error_description}")
                        return@map ""
                    }

                    // 更新AccessToken 和 过期时间
                    account.accessToken = it.access_token
                    account.expiresIn = System.currentTimeMillis() + it.expires_in?.toLong()!! * 1000

                    Log.i(TAG, "requestToken success")
                    GoogleClient.get().accountDatabase.accountDao()
                            .update(account)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                Log.i(GoogleClient.TAG, "update ${account.email} success")
                            }
                    it.access_token
                }
    }

}

