package com.walnut.googlephotosdemo.model

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

class ModelProvider private constructor() {

    companion object {
        @Volatile
        private var viewModelProvider: ViewModelProvider? = null

        @Synchronized
        operator fun invoke(application: Application): ViewModelProvider {
            if (viewModelProvider == null) {
                viewModelProvider = ViewModelProvider(ViewModelStore(), ViewModelProvider.AndroidViewModelFactory(application))
            }
            return viewModelProvider as ViewModelProvider
        }
    }

}