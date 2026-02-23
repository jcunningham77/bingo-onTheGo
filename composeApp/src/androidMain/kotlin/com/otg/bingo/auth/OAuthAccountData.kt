package com.otg.bingo.auth

import com.otg.bingo.repository.internal.OauthProvider

data class OAuthAccountData(
    val idToken: String?,
    val photoUri: String?,
    val displayName: String?,
    val email: String,
    val oAuthProvider: OauthProvider
) {
    override fun toString(): String {
        return """
            idToken: $idToken
            photoUri: $photoUri
            displayName: $displayName
            email: $email
            oAuthProvider: $oAuthProvider
        """.trimIndent()

    }
}