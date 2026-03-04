package com.otg.bingo.repository.internal

import android.content.Context

private object SecureSettingsContextHolder {
    lateinit var appContext: Context
}

fun initSecureSettings(context: Context) {
    SecureSettingsContextHolder.appContext = context.applicationContext
}

actual fun getSecureSettings(): SecureSettings {
    return SecureSettings(SecureSettingsContextHolder.appContext)
}