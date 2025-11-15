package com.douglasessousa.royalehub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.douglasessousa.royalehub.data.local.db.DeckDao

class AppViewModelFactory(private val deckDao: DeckDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(deckDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}