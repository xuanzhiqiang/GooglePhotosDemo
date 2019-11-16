package com.linkplay.googlephotosdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.linkplay.googlephotosdemo.model.AccountModel
import com.linkplay.googlephotosdemo.model.ModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.linkplay.googleapi.GoogleClient


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val RC_SIGN_IN = 9001
        const val PHOTO_SCOPE = "https://www.googleapis.com/auth/photoslibrary"
        const val PHOTO_SCOPE_APPENDONLY = "https://www.googleapis.com/auth/photoslibrary.appendonly"
        const val PHOTO_SCOPE_SHARING = "https://www.googleapis.com/auth/photoslibrary.sharing"
    }

    private val accountModel by lazy {
        ModelProvider(application).get(AccountModel::class.java)
    }

    private val mGoogleSignInClient: GoogleSignInClient? by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id), true)
//                .requestServerAuthCode("520003967142-8t8e73gh1g7l0bakgomvscu7spbvvcp4.apps.googleusercontent.com", true)
                .requestScopes(Scope(PHOTO_SCOPE))
                .requestScopes(Scope(PHOTO_SCOPE_APPENDONLY))
                .requestScopes(Scope(PHOTO_SCOPE_SHARING))
                .requestEmail()
                .build()
        GoogleSignIn.getClient(this, gso)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<SignInButton>(R.id.sign_in_button)
        button.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_LIGHT)
        button.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.signInIntent
                    ?: throw RuntimeException("mGoogleSignInClient is null")
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        findViewById<Button>(R.id.sin_out).setOnClickListener {
            mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
                Log.i(TAG, "signOut success!")
                updateUI(null)
            }
        }

        findViewById<Button>(R.id.refresh_token).setOnClickListener {
            accountModel.refreshToken()
//            mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener {
//                Log.i(TAG, "revokeAccess success!")
//            }
        }

    }

    override fun onStart() {
        super.onStart()
        accountModel.getAccount().observe(this, Observer {
            updateUI(it)

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.i(TAG, "handleSignInResult")
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                Log.i(TAG, "handleSignInResult SignIn Success")
                accountModel.updateAccount(it)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed message: " + e.message)
            Log.w(TAG, "signInResult:failed code: " + e.statusCode)
            e.printStackTrace()
            updateUI(null)
        }
    }


    private fun updateUI(googleAccount: GoogleSignInAccount?) {
        googleAccount?.let {
            Log.i(TAG, "googleAccount: ${it.email}")
            Log.i(TAG, "serverAuthCode: ${it.serverAuthCode}")
            Log.i(TAG, "GoogleClient: ${GoogleClient(application)}")
            Log.i(TAG, "GoogleClient needSignIn: ${GoogleClient(application).needSignIn()}")

            if(!GoogleClient(application).needSignIn()){
                startActivity(Intent(this, AlbumActivity::class.java))
            }

            return
        }
        Log.i(TAG, "account is null")
    }


}

