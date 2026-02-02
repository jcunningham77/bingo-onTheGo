package com.otg.bingo.model

import kotlinx.serialization.SerialName

data class CardTile(
    @SerialName("tile_name") val tileName: String
)
