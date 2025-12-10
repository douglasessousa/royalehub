package com.douglasessousa.royalehub.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.*

class StatsViewModel(repository: RoyaleRepository) : ViewModel() {

    /**
     * Um [stateflow] que expõe a lista de itens de estatística para a ui.
     * É criado combinando os fluxos `alldecks` e `allmatches`.
     */
    val dashboardData = combine(
        repository.allDecks,
        repository.allMatches
    ) { decks, matches ->
        // Dentro deste bloco, os dados são processados e transformados.
        decks.map { deck ->
            // Filtra as partidas que pertencem ao deck atual.
            val deckMatches = matches.filter { it.deckId == deck.id }
            val total = deckMatches.size
            val wins = deckMatches.count { it.isWin }

            // Calcula a taxa de vitória, evitando divisão por zero.
            val winRate = if (total > 0) (wins.toFloat() / total.toFloat()) else 0f

            // Cria o objeto de dados
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