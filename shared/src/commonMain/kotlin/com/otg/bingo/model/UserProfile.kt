package com.otg.bingo.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val displayName: String?,
    val avatarUrl: String?,
    val userId: String? = null,
)
