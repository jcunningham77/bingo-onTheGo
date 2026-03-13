package com.otg.bingo

import android.app.Application
import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.AppComponentImpl
import com.otg.bingo.repository.internal.initSecureSettings

class AndroidApp : Application() {
    val appComponent: AppComponent by lazy { AppComponentImpl() }

    override fun onCreate() {
        super.onCreate()
        initSecureSettings(this.applicationContext)
    }
}
