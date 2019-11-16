package com.linkplay.googlephotosdemo.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.linkplay.googleapi.GoogleClient
import com.linkplay.googlephotosdemo.MainActivity

internal class PhotosViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "PhotosViewModel"
    }

    fun createAlbum(title: String) {
        GoogleClient(getApplication()).createAlbum(title)
    }

    fun listAlbums() {
        Log.i(MainActivity.TAG, "listAlbums GoogleClient: ${GoogleClient(getApplication())}")
        GoogleClient(getApplication()).listAlbums()
    }


}

