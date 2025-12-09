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

/**
 * ViewModel para a tela [CreateDeckScreen].
 *
 *Ele é responsável por:
 * - Carregar as cartas e torres disponíveis da API via [repository].
 * - Manter o estado da UI (cartas selecionadas, nome do deck, etc.) usando [StateFlow].
 * - Processar as ações do usuário, como selecionar uma carta ou salvar o deck.
 * - Realizar a validação dos dados antes de salvar.
 *
 * @property repository O repositório que fornece acesso aos dados (API e banco de dados local).
 */
class DeckViewModel(private val repository: RoyaleRepository) : ViewModel() {

    // Estados privados mutáveis. Apenas o ViewModel pode alterar estes valores.
    private val _availableCards = MutableStateFlow<List<Card>>(emptyList())
    private val _availableTowers = MutableStateFlow<List<Tower>>(emptyList())
    private val _selectedCards = MutableStateFlow<List<Card>>(emptyList())
    private val _selectedTower = MutableStateFlow<Tower?>(null)
    private val _deckName = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _saveError = MutableStateFlow<String?>(null)

    // Estados públicos e imutáveis. A UI observa estes Flows para se atualizar.
    /** A lista de todas as cartas disponíveis para seleção, carregadas da API. */
    val availableCards: StateFlow<List<Card>> = _availableCards.asStateFlow()
    /** A lista de todas as torres disponíveis para seleção, carregadas da API. */
    val availableTowers: StateFlow<List<Tower>> = _availableTowers.asStateFlow()
    /** A lista atual de cartas que o usuário selecionou para o deck. */
    val selectedCards: StateFlow<List<Card>> = _selectedCards.asStateFlow()
    /** A torre que o usuário selecionou para o deck. */
    val selectedTower: StateFlow<Tower?> = _selectedTower.asStateFlow()
    /** O nome do deck que o usuário está digitando. */
    val deckName: StateFlow<String> = _deckName.asStateFlow()
    /** Sinaliza para a UI se uma operação de carregamento está em andamento. */
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    /** Mantém uma mensagem de erro, caso ocorra um problema ao salvar o deck. */
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    /**
     * Bloco de inicialização. É executado assim que o ViewModel é criado.
     * Inicia o carregamento das cartas e torres da API.
     */
    init {
        loadCards()
        loadTowers()
    }

    /**
     * Carrega a lista de cartas da API de forma assíncrona.
     */
    private fun loadCards() {
        viewModelScope.launch {
            _isLoading.value = true
            val cards = repository.getCardsFromApi()
            _availableCards.value = cards
            _isLoading.value = false
        }
    }

    /**
     * Carrega a lista de torres da API de forma assíncrona.
     */
    private fun loadTowers() {
        viewModelScope.launch {
            _isLoading.value = true
            val towers = repository.getTowersFromApi()
            _availableTowers.value = towers
            _isLoading.value = false
        }
    }

    /**
     * Atualiza o nome do deck conforme o usuário digita.
     */
    fun updateDeckName(name: String) {
        _deckName.value = name
    }

    /**
     * Alterna a seleção de uma carta. Se a carta já está no deck, ela é removida.
     * Se não está, ela é adicionada (se o deck não estiver cheio).
     */
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

    /**
     * Substitui uma carta no deck por outra. Usado no modo de troca.
     * @param cardToRemove A carta que está no deck e será removida.
     * @param cardToAdd A nova carta que entrará no lugar.
     */
    fun swapCard(cardToRemove: Card, cardToAdd: Card) {
        val currentList = _selectedCards.value.toMutableList()
        val index = currentList.indexOf(cardToRemove)
        if (index != -1) {
            currentList[index] = cardToAdd
            _selectedCards.value = currentList
        }
    }

    /**
     * Alterna a seleção da torre. Se a torre clicada já é a selecionada, deseleciona.
     * Caso contrário, define-a como a nova torre selecionada.
     */
    fun toggleTowerSelection(tower: Tower) {
        if (_selectedTower.value == tower) {
            _selectedTower.value = null
        } else {
            _selectedTower.value = tower
        }
    }

    /**
     * Tenta salvar o deck atual no banco de dados local.
     * Realiza uma verificação para evitar decks duplicados antes de inserir.
     * @param onSuccess Callback a ser executado se o deck for salvo com sucesso.
     */
    fun saveDeck(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val deckName = _deckName.value
            val selectedCards = _selectedCards.value
            val selectedTower = _selectedTower.value

            // Validação final antes de salvar.
            if (deckName.isNotBlank() && selectedCards.size == 8 && selectedTower != null) {
                val newDeck = Deck(
                    name = deckName,
                    cards = selectedCards,
                    tower = selectedTower
                )

                // Verifica se um deck com mesmo nome ou mesma composição já existe.
                val errorMessage = repository.checkIfDeckExists(newDeck)
                if (errorMessage != null) {
                    _saveError.value = errorMessage
                } else {
                    repository.insertDeck(newDeck)
                    onSuccess() // Notifica a UI para navegar para trás.
                }
            }
        }
    }

    /**
     * Limpa a mensagem de erro de salvamento, fazendo o diálogo de erro desaparecer.
     */
    fun clearSaveError() {
        _saveError.value = null
    }
}
