package com.otg.bingo.repository.internal

expect fun getSecureSettings(): SecureSettings
expect class SecureSettings {
    fun putString(key: String, value: String)
    fun getStringOrNull(key: String): String?
    fun remove(key: String)
}