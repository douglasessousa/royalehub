package com.douglasessousa.royalehub.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.*

class StatsViewModel(repository: RoyaleRepository) : ViewModel() {

    val dashboardData = combine(
        repository.allDecks,
        repository.allMatches
    ) { decks, matches ->

        // Calcula as estatísticas para cada deck
        decks.map { deck ->
            val deckMatches = matches.filter { it.deckId == deck.id }
            val total = deckMatches.size
            val wins = deckMatches.count { it.isWin }
            val winRate = if (total > 0) (wins.toFloat() / total.toFloat()) else 0f

            DeckDashboardItem(
                deckName = deck.name,
                winRate = winRate,
                totalMatches = total,
                wins = wins,
                losses = total - wins
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

data class DeckDashboardItem(
    val deckName: String,
    val winRate: Float,
    val totalMatches: Int,
    val wins: Int,
    val losses: Int
)