package com.douglasessousa.royalehub.data.model

data class Deck(
    val id: Int,
    val nome: String,
    val cartas: List<Carta>
)