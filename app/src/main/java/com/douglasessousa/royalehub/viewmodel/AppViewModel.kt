package com.douglasessousa.royalehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.api.ApiClient
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _cards = MutableStateFlow<List<Carta>>(emptyList())
    val cards: StateFlow<List<Carta>> = _cards

    private val _towers = MutableStateFlow<List<TropaDeTorre>>(emptyList())
    val towers: StateFlow<List<TropaDeTorre>> = _towers

    init {
        fetchCards()
        fetchTowers()
    }

    private fun fetchCards() {
        viewModelScope.launch {
            try {
                _cards.value = ApiClient.apiService.getCards()
            } catch (e: Exception) {
                println("Error fetching cards: ${e.message}")
            }
        }
    }

    private fun fetchTowers() {
        viewModelScope.launch {
            try {
                _towers.value = ApiClient.apiService.getTowers()
            } catch (e: Exception) {
                println("Error fetching towers: ${e.message}")
            }
        }
    }
}
