package com.otg.bingo

import com.otg.bingo.di.AppComponent
import com.otg.bingo.di.AppComponentImpl

object IosApp {
    val appComponent: AppComponent by lazy { AppComponentImpl() }
}