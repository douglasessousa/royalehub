package com.douglasessousa.royalehub.data.model

import com.google.gson.annotations.SerializedName

data class Tower(
    val id: Int,

    @SerializedName("nome")
    val name: String,

    @SerializedName("raridade")
    val rarity: String,

    @SerializedName("imagemUrl")
    val imageUrl: String
)