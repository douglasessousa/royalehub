package com.douglasessousa.royalehub.data.repository

import android.util.Log
import com.douglasessousa.royalehub.api.RoyaleApiService
import com.douglasessousa.royalehub.data.local.db.RoyaleDao
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.data.model.User
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

    suspend fun checkIfDeckExists(deck: Deck): String? {
        if (dao.deckExistsByName(deck.name)) {
            return "Você já tem um deck com esse nome."
        }

        val allDecks = dao.getAllDecksSuspend()
        val newDeckCards = deck.cards.map { it.id }.toSet()

        for (existingDeck in allDecks) {
            val existingDeckCards = existingDeck.cards.map { it.id }.toSet()
            if (existingDeckCards == newDeckCards && existingDeck.tower?.id == deck.tower?.id) {
                return "Já existe um deck com essas mesmas cartas e torre"
            }
        }

        return null
    }

    // User functions
    fun getUser(): Flow<User?> = dao.getUser()

    suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    // Deck functions
    val allDecks: Flow<List<Deck>> = dao.getDecks()

    suspend fun getDeckById(id: Int): Deck? {
        return dao.getDeckById(id)
    }

    suspend fun insertDeck(deck: Deck) {
        dao.insertDeck(deck)
    }

    suspend fun deleteDeck(deck: Deck) {
        dao.deleteMatchesByDeckId(deck.id)
        dao.deleteDeck(deck)
    }

    // Match functions
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

    val allMatches: Flow<List<MatchResult>> = dao.getAllMatches()

    // Data cleanup
    suspend fun clearAllData() {
        dao.deleteAllMatches()
        dao.deleteAllDecks()
        dao.deleteAllUsers() // Assuming this function will be added to the DAO
    }
}