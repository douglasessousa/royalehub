package com.douglasessousa.royalehub.data.repository

import android.util.Log
import com.douglasessousa.royalehub.api.RoyaleApiService
import com.douglasessousa.royalehub.data.local.db.RoyaleDao
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import kotlinx.coroutines.flow.Flow

class RoyaleRepository(
    private val dao: RoyaleDao,
    private val api: RoyaleApiService
) {

    suspend fun getCardsFromApi(): List<Card> {
        return try {
            api.getAllCards()
        } catch (e: Exception) {
            Log.e("RoyaleRepository", "Erro ao buscar cartas: ${e.message}")
            emptyList()
        }
    }

    suspend fun getTowersFromApi(): List<Tower> {
        return try {
            api.getAllTowers()
        } catch (e: Exception) {
            Log.e("RoyaleRepository", "Erro ao buscar tropas de torre: ${e.message}")
            emptyList()
        }
    }

    // O Flow notifica a UI sempre que houver mudanças na tabela
    val allDecks: Flow<List<Deck>> = dao.getDecks()

    suspend fun getDeckById(id: Int): Deck? {
        return dao.getDeckById(id)
    }

    suspend fun insertDeck(deck: Deck) {
        dao.insertDeck(deck)
    }

    suspend fun deleteDeck(deck: Deck) {
        // Ao apagar um deck, remove também o histórico de partidas
        dao.deleteMatchesByDeckId(deck.id)
        dao.deleteDeck(deck)
    }

    val recentMatches: Flow<List<MatchResult>> = dao.getRecentMatches()

    fun getMatchesForDeck(deckId: Int): Flow<List<MatchResult>> {
        return dao.getMatchesForDeck(deckId)
    }

    suspend fun insertMatch(match: MatchResult) {
        dao.insertMatch(match)
    }

    suspend fun deleteMatch(match: MatchResult) {
        dao.deleteMatch(match)
    }

    val allMatches: Flow<List<MatchResult>> = dao.getAllMatches();

    suspend fun clearAllData() {
        dao.deleteAllMatches()
        dao.deleteAllDecks()
    }
}