package com.douglasessousa.royalehub.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: RoyaleRepository) : ViewModel() {
    fun clearData() {
        viewModelScope.launch { repository.clearAllData() }
    }
}