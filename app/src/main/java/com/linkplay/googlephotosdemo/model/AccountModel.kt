package com.linkplay.googlephotosdemo.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.linkplay.googleapi.GoogleClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class AccountModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "AccountModel"
    }

    private val account: MutableLiveData<GoogleSignInAccount> by lazy {
        MutableLiveData<GoogleSignInAccount>().apply {
            value = GoogleSignIn.getLastSignedInAccount(getApplication())
        }
    }



    fun getAccount(): LiveData<GoogleSignInAccount> = account

    fun updateAccount(account: GoogleSignInAccount) {
        this.account.postValue(account)
        account.serverAuthCode?.let {
            Log.i(TAG, "update serverAuthCode: $it")
            GoogleClient(getApplication()).requestToken(it)
        }
    }

    fun refreshToken(){
        GoogleClient(getApplication()).refreshToken()
    }

}