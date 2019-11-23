package com.walnut.googlephotosdemo.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.walnut.googleapi.GoogleClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.walnut.googleapi.data.Account
import com.walnut.googleapi.network.AccountManager

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
        GoogleClient.get().changePhotosAccount(Account(
                id = account.id!!,
                email = account.email,
                givenName = account.givenName,
                familyName = account.familyName,
                displayName = account.displayName,
                photoUrl = account.photoUrl.toString()
        ))
        account.serverAuthCode?.let {
            Log.i(TAG, "update serverAuthCode: $it")
            GoogleClient.get().requestToken(it)
        }
    }

    fun refreshToken(){
//        GoogleClient.get().refreshToken()
    }

}