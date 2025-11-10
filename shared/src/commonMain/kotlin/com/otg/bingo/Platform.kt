package com.otg.bingo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform