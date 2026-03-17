package com.otg.bingo.auth

import com.otg.bingo.repository.internal.OauthProvider

data class OAuthAccountData(
    val idToken: String?,
    val oAuthProvider: OauthProvider,
) {
    override fun toString(): String =
        """
        idToken: $idToken
        oAuthProvider: $oAuthProvider
        """.trimIndent()
}
