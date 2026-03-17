package com.otg.bingo.repository.internal

import platform.Foundation.NSUserDefaults

actual class SecureSettings {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getStringOrNull(key: String): String? =
        defaults.stringForKey(key)

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}
