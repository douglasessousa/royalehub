package com.douglasessousa.royalehub.ui.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeckViewModel(private val repository: RoyaleRepository) : ViewModel() {

    // Estado das cartas disponíveis
    private val _availableCards = MutableStateFlow<List<Card>>(emptyList())
    val availableCards: StateFlow<List<Card>> = _availableCards.asStateFlow()

    // Estado das cartas selecionadas pelo usuário
    private val _selectedCards = MutableStateFlow<List<Card>>(emptyList())
    val selectedCards: StateFlow<List<Card>> = _selectedCards.asStateFlow()

    private val _deckName = MutableStateFlow("")
    val deckName: StateFlow<String> = _deckName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() {
        viewModelScope.launch {
            _isLoading.value = true
            val cards = repository.getCardsFromApi()
            _availableCards.value = cards
            _isLoading.value = false
        }
    }

    fun updateDeckName(name: String) {
        _deckName.value = name
    }

    // Adiciona ou remove carta
    fun toggleCardSelection(card: Card) {
        val currentList = _selectedCards.value.toMutableList()

        if (currentList.contains(card)) {
            // Se já tem, remove
            currentList.remove(card)
        } else {
            // Se não tem e ainda cabe (max 8), adiciona
            if (currentList.size < 8) {
                currentList.add(card)
            }
        }
        _selectedCards.value = currentList
    }

    // Salva no database
    fun saveDeck(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (_deckName.value.isNotBlank() && _selectedCards.value.size == 8) {
                val newDeck = Deck(
                    name = _deckName.value,
                    cards = _selectedCards.value
                )
                repository.insertDeck(newDeck)
                onSuccess()
            }
        }
    }
}