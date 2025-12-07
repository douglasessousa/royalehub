package com.douglasessousa.royalehub.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglasessousa.royalehub.data.model.User
import com.douglasessousa.royalehub.data.repository.RoyaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: RoyaleRepository) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUser().first()?.let {
                _user.value = it
                _nickname.value = it.nickname
                _id.value = it.id
            }
        }
    }

    fun onNicknameChange(nickname: String) {
        _nickname.value = nickname
    }

    fun onIdChange(id: String) {
        _id.value = id
    }

    fun saveUser() {
        viewModelScope.launch {
            // Usa o avatarUrl existente se houver, ou uma string vazia
            val currentAvatarUrl = _user.value?.avatarUrl ?: ""
            val userToSave = User(id = _id.value, nickname = _nickname.value, avatarUrl = currentAvatarUrl)
            repository.insertUser(userToSave)
        }
    }

    fun clearData() {
        viewModelScope.launch { repository.clearAllData() }
    }
}