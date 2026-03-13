package com.otg.bingo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardTile(
    @SerialName("tile_name") val tileName: String,
)
