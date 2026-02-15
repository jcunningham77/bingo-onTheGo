package com.otg.bingo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameTheme(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("imgUrl") val imgUrl: String
)