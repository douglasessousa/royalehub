package com.douglasessousa.royalehub.data.local

import com.douglasessousa.royalehub.data.model.Deck

object LocalRepository {

    private val mockDecks = listOf(
        Deck(
            id = 1,
            nome = "Log Bait",
            cartas = listOf()
        ),
        Deck(
            id = 2,
            nome = "Splashyard",
            cartas = listOf()
        ),
        Deck(
            id = 3,
            nome = "Corredor 2.6",
            cartas = listOf()
        ),
        Deck(
            id = 4,
            nome = "Porcos Reais",
            cartas = listOf()
        )
    )

    fun getDecks(): List<Deck> = mockDecks
}