package com.douglasessousa.royalehub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * O Deck do banco de dados só tem Nome e Cartas.
 * Mas a Tela precisa mostrar o Win Rate e o Elixir.
 * Então, este objeto junta tudo num lugar só para facilitar o desenho da tela.
 */
data class DeckUiState(
    val deck: Deck, // O deck original
    val winRate: Float, // A taxa de vitória calculada (0.0 a 1.0)
    val totalMatches: Int, // Quantas vezes jogaste com ele
    val averageElixir: Double // Custo médio de elixir
)

class HomeViewModel(repository: RoyaleRepository) : ViewModel() {

    /**
     * A função combine observa duas fontes de dados ao mesmo tempo:
     * - A lista de Decks
     * - A lista de Partidas
     *
     * Se qualquer uma delas mudar, este bloco roda automaticamente de novo.
     */
    val decksState: StateFlow<List<DeckUiState>> = combine(
        repository.allDecks,
        repository.allMatches
    ) { decks, matches ->

        // Para cada deck q existe no banco
        decks.map { deck ->
            val deckMatches = matches.filter { it.deckId == deck.id }
            val total = deckMatches.size
            val wins = deckMatches.count { it.isWin }

            // Evita divisão por zero, se não tiver partidas, o Win Rate é 0.
            val winRate = if (total > 0) wins.toFloat() / total else 0f

            // Soma o elixir de todas as cartas e dividi pela quantidade (8, pois a torre n tem elixir)
            val avgElixir = if (deck.cards.isNotEmpty()) {
                deck.cards.sumOf { it.elixir }.toDouble() / deck.cards.size
            } else 0.0

            // Cria o objeto final que a tela vai receber
            DeckUiState(
                deck = deck,
                winRate = winRate,
                totalMatches = total,
                averageElixir = avgElixir
            )
        }
    }.stateIn(
        scope = viewModelScope, // cálculo morre quando o ViewModel morre
        started = SharingStarted.WhileSubscribed(5000), // mantém vivo por 5s se a tela girar
        initialValue = emptyList() // começa com lista vazia enquanto carrega
    )
}