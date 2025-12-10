package com.douglasessousa.royalehub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import com.douglasessousa.royalehub.ui.deck.DeckViewModel
import com.douglasessousa.royalehub.ui.deck_details.DeckDetailsViewModel
import com.douglasessousa.royalehub.ui.home.HomeViewModel
import com.douglasessousa.royalehub.ui.settings.SettingsViewModel
import com.douglasessousa.royalehub.ui.stats.StatsViewModel

/**
 * Esta classe ensina o Android a "fabricar" nossos ViewModels,
 * injetando o Repositório dentro deles.
 */
class ViewModelFactory(private val repository: RoyaleRepository) : ViewModelProvider.Factory {

    // O Suppress é para garantir q o compilador confie no código e q T é um ViewModel
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Como o 'if' deu verdadeiro, então nós sabemos que é seguro converter.
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DeckViewModel::class.java)) {
            return DeckViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DeckDetailsViewModel::class.java)) {
            return DeckDetailsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            return StatsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}