package com.otg.bingo.util


fun Any.loggi(message: String) {
    println("[${tagOf(this)}] JRC123 $message")
}

fun loggi(message: String) {
    println("JRC123 $message")
}



private fun tagOf(any: Any): String = any::class.simpleName ?: "Unknown"
