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

/**
 * Cérebro que decide de onde os dados vêm e para onde vão.
 * dao (Banco de Dados Local - Room): Para guardar coisas no celular.
 * api (Retrofit): Para baixar coisas do servidor Node.js.
 */
class RoyaleRepository(
    private val dao: RoyaleDao,
    private val api: RoyaleApiService
) {

    /**
     * Busca a lista de todas as cartas no servidor.
     * Se der erro, retorna uma lista vazia.
     */
    suspend fun getCardsFromApi(): List<Card> {
        return try {
            api.getAllCards()
        } catch (e: Exception) {
            Log.e("RoyaleRepository", "Erro ao buscar cartas: ${e.message}")
            emptyList()
        }
    }

    /**
     * Busca a lista de todas as torres no servidor.
     * Se der erro, retorna uma lista vazia.
     */
    suspend fun getTowersFromApi(): List<Tower> {
        return try {
            api.getAllTowers()
        } catch (e: Exception) {
            Log.e("RoyaleRepository", "Erro ao buscar tropas de torre: ${e.message}")
            emptyList()
        }
    }

    /**
     * Antes de salvar um deck, verifica se ele já existe.
     * - Não pode ter o mesmo nome.
     * - Não pode ter exatamente as mesmas 8 cartas + a mesma torre (mesmo com nome diferente).
     *
     * Retorna a String com o erro se tiver problema, ou null se estiver tudo certo.
     */
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

    // Fica de olho no usuário atual, o Flow avisa se mudar
    fun getUser(): Flow<User?> = dao.getUser()

    // Salva ou atualiza os dados do perfil (foto, nick, id)
    suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    // Uma lista viva de decks. Se adicionar um deck novo, a tela atualiza sozinha.
    val allDecks: Flow<List<Deck>> = dao.getDecks()

    // Busca um deck específico pelo número de ID
    suspend fun getDeckById(id: Int): Deck? {
        return dao.getDeckById(id)
    }

    // Grava o deck no celular
    suspend fun insertDeck(deck: Deck) {
        dao.insertDeck(deck)
    }

    // Apaga o deck e tbm o histórico de partidas desse deck
    suspend fun deleteDeck(deck: Deck) {
        dao.deleteMatchesByDeckId(deck.id)
        dao.deleteDeck(deck)
    }

    // Pega as últimas partidas
    val recentMatches: Flow<List<MatchResult>> = dao.getRecentMatches()

    // Pega as partidas só de um deck específico
    fun getMatchesForDeck(deckId: Int): Flow<List<MatchResult>> {
        return dao.getMatchesForDeck(deckId)
    }

    // Registra uma nova batalha
    suspend fun insertMatch(match: MatchResult) {
        dao.insertMatch(match)
    }

    // Apaga uma partida específica
    suspend fun deleteMatch(match: MatchResult) {
        dao.deleteMatch(match)
    }

    // Pega TODAS as partidas já jogadas
    val allMatches: Flow<List<MatchResult>> = dao.getAllMatches()

    // Limpa todas as tabelas do banco de dados
    suspend fun clearAllData() {
        dao.deleteAllMatches()
        dao.deleteAllDecks()
        dao.deleteAllUsers()
    }
}