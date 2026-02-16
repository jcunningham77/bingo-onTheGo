package com.otg.bingo

import android.app.Application
import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.AppComponentImpl

class AndroidApp : Application(){
    val appComponent: AppComponent by lazy { AppComponentImpl() }
}