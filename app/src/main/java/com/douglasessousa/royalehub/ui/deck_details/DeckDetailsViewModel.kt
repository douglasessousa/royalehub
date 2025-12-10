package com.douglasessousa.royalehub.ui.deck_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel q gerencia a tela q mostra um único deck.
 * As suas responsabilidades são:
 * - Carregar as informações do Deck (Nome, Cartas).
 * - Carregar o Histórico de Partidas desse Deck.
 * - Calcular as Estatísticas (Win Rate) em tempo real.
 */
class DeckDetailsViewModel(private val repository: RoyaleRepository) : ViewModel() {

    // _deck é mutável e privado (só o ViewModel mexe).
    private val _deck = MutableStateFlow<Deck?>(null)
    // deck é público e imutável (a Tela só lê).
    val deck: StateFlow<Deck?> = _deck.asStateFlow()

    private val _matches = MutableStateFlow<List<MatchResult>>(emptyList())
    val matches: StateFlow<List<MatchResult>> = _matches.asStateFlow()

    /**
     * A lista de partidas (_matches) é observada.
     * Toda vez q uma partida entra ou sai, este bloco 'map' roda sozinho e recalcula tudo.
     */
    val stats = _matches.map { matchesList ->
        val total = matchesList.size
        val wins = matchesList.count { it.isWin }
        val losses = total - wins

        // Evita divisão por zero.
        val winRate = if (total > 0) (wins.toFloat() / total.toFloat()) else 0f

        // Retorna um objeto simples com os números prontos
        DeckStats(total, wins, losses, winRate)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeckStats(0, 0, 0, 0f)
    )

    /**
     * Chamado assim q a tela abre.
     * Busca o deck pelo ID e conecta o Flow de partidas.
     */
    fun loadDeck(deckId: Int) {
        viewModelScope.launch {
            // Pega os dados do deck (Nome, Cartas)
            _deck.value = repository.getDeckById(deckId)

            // Fica observando o banco de dados pra atualizações nas partidas
            repository.getMatchesForDeck(deckId).collect { list ->
                _matches.value = list
            }
        }
    }

    // Adiciona uma nova vitória ou derrota
    fun addMatch(isWin: Boolean) {
        val currentDeckId = _deck.value?.id ?: return // se não tiver deck carregado, para.
        viewModelScope.launch {
            val match = MatchResult(
                deckId = currentDeckId,
                isWin = isWin
            )
            repository.insertMatch(match)
        }
    }

    // Remove uma partida do histórico
    fun deleteMatch(match: MatchResult) {
        viewModelScope.launch {
            repository.deleteMatch(match)
        }
    }

    // Apaga o Deck inteiro e volta pra a tela anterior
    fun deleteDeck(onDeleted: () -> Unit) {
        val currentDeck = _deck.value ?: return
        viewModelScope.launch {
            repository.deleteDeck(currentDeck)
            onDeleted() // Chama a função de navegação (popBackStack)
        }
    }
}

// Classe pra agrupar os dados
data class DeckStats(val total: Int, val wins: Int, val losses: Int, val winRate: Float)