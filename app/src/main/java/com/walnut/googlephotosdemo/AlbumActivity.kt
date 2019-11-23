package com.walnut.googlephotosdemo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.walnut.googlephotosdemo.model.PhotosViewModel

class AlbumActivity : AppCompatActivity() {

    companion object {
        const val TAG = "AlbumActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val photosViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(PhotosViewModel::class.java)

        findViewById<Button>(R.id.create_album).setOnClickListener {
            photosViewModel.createAlbum("789")
        }

        findViewById<Button>(R.id.list_album).setOnClickListener {
            photosViewModel.listAlbums()
        }

    }

}