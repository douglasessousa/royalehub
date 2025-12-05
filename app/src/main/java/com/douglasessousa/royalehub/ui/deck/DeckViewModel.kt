package com.douglasessousa.royalehub.ui.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeckViewModel(private val repository: RoyaleRepository) : ViewModel() {

    private val _availableCards = MutableStateFlow<List<Card>>(emptyList())
    val availableCards: StateFlow<List<Card>> = _availableCards.asStateFlow()

    private val _availableTowers = MutableStateFlow<List<Tower>>(emptyList())
    val availableTowers: StateFlow<List<Tower>> = _availableTowers.asStateFlow()

    private val _selectedCards = MutableStateFlow<List<Card>>(emptyList())
    val selectedCards: StateFlow<List<Card>> = _selectedCards.asStateFlow()

    private val _selectedTower = MutableStateFlow<Tower?>(null)
    val selectedTower: StateFlow<Tower?> = _selectedTower.asStateFlow()

    private val _deckName = MutableStateFlow("")
    val deckName: StateFlow<String> = _deckName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    init {
        loadCards()
        loadTowers()
    }

    private fun loadCards() {
        viewModelScope.launch {
            _isLoading.value = true
            val cards = repository.getCardsFromApi()
            _availableCards.value = cards
            _isLoading.value = false
        }
    }

    private fun loadTowers() {
        viewModelScope.launch {
            _isLoading.value = true
            val towers = repository.getTowersFromApi()
            _availableTowers.value = towers
            _isLoading.value = false
        }
    }

    fun updateDeckName(name: String) {
        _deckName.value = name
    }

    fun toggleCardSelection(card: Card) {
        val currentList = _selectedCards.value.toMutableList()
        if (currentList.contains(card)) {
            currentList.remove(card)
        } else {
            if (currentList.size < 8) {
                currentList.add(card)
            }
        }
        _selectedCards.value = currentList
    }

    fun swapCard(cardToRemove: Card, cardToAdd: Card) {
        val currentList = _selectedCards.value.toMutableList()
        val index = currentList.indexOf(cardToRemove)
        if (index != -1) {
            currentList[index] = cardToAdd
            _selectedCards.value = currentList
        }
    }

    fun toggleTowerSelection(tower: Tower) {
        if (_selectedTower.value == tower) {
            _selectedTower.value = null
        } else {
            _selectedTower.value = tower
        }
    }

    fun saveDeck(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val deckName = _deckName.value
            val selectedCards = _selectedCards.value
            val selectedTower = _selectedTower.value

            if (deckName.isNotBlank() && selectedCards.size == 8 && selectedTower != null) {
                val newDeck = Deck(
                    name = deckName,
                    cards = selectedCards,
                    tower = selectedTower
                )

                val errorMessage = repository.checkIfDeckExists(newDeck)
                if (errorMessage != null) {
                    _saveError.value = errorMessage
                } else {
                    repository.insertDeck(newDeck)
                    onSuccess()
                }
            }
        }
    }

    fun clearSaveError() {
        _saveError.value = null
    }
}
