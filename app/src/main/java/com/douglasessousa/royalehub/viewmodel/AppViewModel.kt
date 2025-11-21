package com.douglasessousa.royalehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.api.ApiClient
import com.douglasessousa.royalehub.data.local.db.DeckDao
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val deckDao: DeckDao) : ViewModel() {

    // API data
    private val _cards = MutableStateFlow<List<Carta>>(emptyList())
    val cards: StateFlow<List<Carta>> = _cards

    private val _towers = MutableStateFlow<List<TropaDeTorre>>(emptyList())
    val towers: StateFlow<List<TropaDeTorre>> = _towers

    // Database data
    val decks: StateFlow<List<Deck>> = deckDao.getAllDecks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        fetchCards()
        fetchTowers()
    }

    fun upsertDeck(deck: Deck) {
        viewModelScope.launch {
            deckDao.upsertDeck(deck)
        }
    }

    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            deckDao.deleteDeck(deck)
        }
    }

    private fun fetchCards() {
        viewModelScope.launch {
            try {
                _cards.value = ApiClient.apiService.getCards()
            } catch (e: Exception) {
                // TODO: Expor o erro para a UI de forma mais elegante
                println("Error fetching cards: ${e.message}")
            }
        }
    }

    private fun fetchTowers() {
        viewModelScope.launch {
            try {
                _towers.value = ApiClient.apiService.getTowers()
            } catch (e: Exception) {
                // TODO: Expor o erro para a UI de forma mais elegante
                println("Error fetching towers: ${e.message}")
            }
        }
    }
}
