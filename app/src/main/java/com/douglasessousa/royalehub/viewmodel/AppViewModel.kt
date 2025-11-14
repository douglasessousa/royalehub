package com.douglasessousa.royalehub.viewmodel

import androidx.lifecycle.ViewModel
import com.douglasessousa.royalehub.data.local.*

class AppViewModel : ViewModel() {
    val decks = LocalRepository.getDecks()
}