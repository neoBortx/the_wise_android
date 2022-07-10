package com.bortxapps.thewise.injection

import android.app.Application
import com.bortxapps.application.injection.ServiceModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TheWiseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}