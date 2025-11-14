package com.douglasessousa.royalehub.data.local

import com.douglasessousa.royalehub.data.model.Deck

object LocalRepository {

    private val mockDecks = listOf(
        Deck(
            id = 1,
            nome = "Deck 1",
            cartas = listOf()
        ),
        Deck(
            id = 2,
            nome = "Deck 2",
            cartas = listOf()
        )
    )

    fun getDecks(): List<Deck> = mockDecks
}