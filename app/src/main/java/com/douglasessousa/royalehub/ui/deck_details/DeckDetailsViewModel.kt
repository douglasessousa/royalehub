package com.douglasessousa.royalehub.ui.deck_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DeckDetailsViewModel(private val repository: RoyaleRepository) : ViewModel() {

    private val _deck = MutableStateFlow<Deck?>(null)
    val deck: StateFlow<Deck?> = _deck.asStateFlow()

    private val _matches = MutableStateFlow<List<MatchResult>>(emptyList())
    val matches: StateFlow<List<MatchResult>> = _matches.asStateFlow()

    // Estatísticas
    val stats = _matches.map { matchesList ->
        val total = matchesList.size
        val wins = matchesList.count { it.isWin }
        val losses = total - wins
        val winRate = if (total > 0) (wins.toFloat() / total.toFloat()) else 0f

        DeckStats(total, wins, losses, winRate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DeckStats(0, 0, 0, 0f))

    // Carrega o deck e começa a observar as partidas dele
    fun loadDeck(deckId: Int) {
        viewModelScope.launch {
            _deck.value = repository.getDeckById(deckId)

            // observa as partidas (o Flow atualiza automático)
            repository.getMatchesForDeck(deckId).collect { list ->
                _matches.value = list
            }
        }
    }

    // Adiciona uma partida
    fun addMatch(isWin: Boolean) {
        val currentDeckId = _deck.value?.id ?: return
        viewModelScope.launch {
            val match = MatchResult(
                deckId = currentDeckId,
                isWin = isWin
            )
            repository.insertMatch(match)
        }
    }

    // Apaga uma partida do histórico
    fun deleteMatch(match: MatchResult) {
        viewModelScope.launch {
            repository.deleteMatch(match)
        }
    }

    // Apaga o Deck inteiro
    fun deleteDeck(onDeleted: () -> Unit) {
        val currentDeck = _deck.value ?: return
        viewModelScope.launch {
            repository.deleteDeck(currentDeck)
            onDeleted()
        }
    }
}

data class DeckStats(val total: Int, val wins: Int, val losses: Int, val winRate: Float)