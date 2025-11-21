package com.douglasessousa.royalehub.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.douglasessousa.royalehub.data.model.Deck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Query("SELECT * FROM decks")
    fun getAllDecks(): Flow<List<Deck>>

    @Upsert
    suspend fun upsertDeck(deck: Deck)

    @Delete
    suspend fun deleteDeck(deck: Deck)
}