package com.walnut.googlephotosdemo.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.walnut.googleapi.GoogleClient
import com.walnut.googlephotosdemo.MainActivity

internal class PhotosViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "PhotosViewModel"
    }

    fun createAlbum(title: String) {
        GoogleClient.get().createAlbum(title)
    }

    fun listAlbums() {
        GoogleClient.get().listAlbums()
    }


}

