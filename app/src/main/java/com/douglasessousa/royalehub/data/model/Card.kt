package com.douglasessousa.royalehub.data.model

import com.google.gson.annotations.SerializedName

data class Card(
    val id: Int,

    @SerializedName("nome")
    val name: String,

    val elixir: Int = 0,

    @SerializedName("raridade")
    val rarity: String,

    @SerializedName("imagemUrl")
    val imageUrl: String
)