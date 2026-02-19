package com.otg.bingo.repository.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdTokenGrantRequest(
    val provider: String,
    @SerialName("id_token") val idToken: String
)