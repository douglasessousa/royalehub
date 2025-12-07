package com.douglasessousa.royalehub.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RoyaleDao {

    // Pega todos os decks salvos
    @Query("SELECT * FROM decks")
    fun getDecks(): Flow<List<Deck>>

    @Query("SELECT * FROM decks")
    suspend fun getAllDecksSuspend(): List<Deck>

    @Query("SELECT EXISTS(SELECT 1 FROM decks WHERE name = :name LIMIT 1)")
    suspend fun deckExistsByName(name: String): Boolean

    // Pega um deck específico pelo ID
    @Query("SELECT * FROM decks WHERE id = :id")
    suspend fun getDeckById(id: Int): Deck?

    // Insere ou atualiza um deck
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Deck)

    // Delete um deck
    @Delete
    suspend fun deleteDeck(deck: Deck)

    // Pega as últimas 20 partidas, ordenadas da mais recente para a mais antiga
    @Query("SELECT * FROM matches ORDER BY timestamp DESC LIMIT 20")
    fun getRecentMatches(): Flow<List<MatchResult>>

    // Pega todas as partidas de um deck específico (para calcular Win Rate)
    @Query("SELECT * FROM matches WHERE deckId = :deckId")
    fun getMatchesForDeck(deckId: Int): Flow<List<MatchResult>>

    @Insert
    suspend fun insertMatch(match: MatchResult)

    @Delete
    suspend fun deleteMatch(match: MatchResult)

    // Limpa as partidas antigas se deletarmos um deck
    @Query("DELETE FROM matches WHERE deckId = :deckId")
    suspend fun deleteMatchesByDeckId(deckId: Int)

    @Query("SELECT * FROM matches")
    fun getAllMatches(): Flow<List<MatchResult>>

    @Query("DELETE FROM decks")
    suspend fun deleteAllDecks()

    @Query("DELETE FROM matches")
    suspend fun deleteAllMatches()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUser(): Flow<User?>
    
    @Query("DELETE FROM user_profile")
    suspend fun deleteAllUsers()
}