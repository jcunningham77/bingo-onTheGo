package com.otg.bingo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameTheme(
    @SerialName("name") val name: String,
    @SerialName("imgUrl") val imgUrl: String
)