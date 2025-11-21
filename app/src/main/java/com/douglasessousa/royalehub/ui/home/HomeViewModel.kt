package com.douglasessousa.royalehub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DeckUiState(
    val deck: Deck,
    val winRate: Float,
    val totalMatches: Int,
    val averageElixir: Double
)

class HomeViewModel(repository: RoyaleRepository) : ViewModel() {

    val decksState: StateFlow<List<DeckUiState>> = combine(
        repository.allDecks,
        repository.allMatches
    ) { decks, matches ->
        decks.map { deck ->
            // Calcula Win Rate
            val deckMatches = matches.filter { it.deckId == deck.id }
            val total = deckMatches.size
            val wins = deckMatches.count { it.isWin }
            val winRate = if (total > 0) wins.toFloat() / total else 0f

            // Calcula Elixir Médio
            val avgElixir = if (deck.cards.isNotEmpty()) {
                deck.cards.sumOf { it.elixir }.toDouble() / deck.cards.size
            } else 0.0

            DeckUiState(
                deck = deck,
                winRate = winRate,
                totalMatches = total,
                averageElixir = avgElixir
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}