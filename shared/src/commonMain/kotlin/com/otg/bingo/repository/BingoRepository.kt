package com.otg.bingo.repository

interface BingoRepository {
    fun getGameThemes(): Flow<List<String>>
}