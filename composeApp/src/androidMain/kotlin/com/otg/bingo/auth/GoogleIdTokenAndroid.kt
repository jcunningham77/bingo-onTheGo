package com.otg.bingo.auth

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.otg.bingo.repository.internal.OauthProvider

class GoogleIdTokenAndroid(
    activity: Activity,
    private val webClientId: String
) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(activity)

    fun beginSignIn(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        onError: (Throwable) -> Unit
    ) {
        val request = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(false)
            .build()

        oneTapClient.beginSignIn(request)
            .addOnSuccessListener { result ->
                val intentSender: IntentSender = result.pendingIntent.intentSender
                launcher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener { onError(it) }
    }

    fun extractOauthAccountData(data: Intent?): OAuthAccountData {
        val credential = oneTapClient.getSignInCredentialFromIntent(data)
        val photoUri: Uri? = credential.profilePictureUri

        return OAuthAccountData(
            idToken = credential.googleIdToken,
            photoUri = photoUri,
            displayName = credential.displayName,
            email = credential.id,
            OauthProvider.GOOGLE,
        )
    }

    data class OAuthAccountData(
        val idToken: String?,
        val photoUri: Uri?,
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
}