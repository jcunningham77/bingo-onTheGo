package com.otg.bingo.repository

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header

private const val API_KEY_KEY = "apiKey"
private const val API_KEY_VALUE = "sb_publishable_pNiCZbjQKm-q_l6_bcKN-w_qE-JwxkU"

private const val AUTHORIZATION_KEY = "Authorization"
private const val AUTHORIZATION_VALUE = "Bearer $API_KEY_VALUE"
internal const val SUPABASE_HOST = "https://qwldabjzqgwyxihictth.supabase.co"
fun HttpRequestBuilder.addSupabaseHeaders() {
    header(AUTHORIZATION_KEY, AUTHORIZATION_VALUE)
    header(API_KEY_KEY, API_KEY_VALUE)
}