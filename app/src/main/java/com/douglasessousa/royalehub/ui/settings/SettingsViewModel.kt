package com.douglasessousa.royalehub.ui.settings

import android.net.Uri
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

    // Este estado agora representa o 'gameId'
    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri = _avatarUri.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUser().first()?.let {
                _user.value = it
                _nickname.value = it.nickname
                _id.value = it.gameId // Corrigido para usar gameId
                if (it.avatarUrl.isNotEmpty()) {
                    _avatarUri.value = it.avatarUrl
                }
            }
        }
    }

    fun onNicknameChange(nickname: String) {
        _nickname.value = nickname
    }

    fun onIdChange(id: String) {
        _id.value = id
    }

    fun onAvatarChange(uri: Uri?) {
        _avatarUri.value = uri?.toString()
    }

    fun saveUser() {
        viewModelScope.launch {
            val userToSave = User(
                pk = 0, // Chave primária fixa
                gameId = _id.value, // Corrigido para gameId
                nickname = _nickname.value,
                avatarUrl = _avatarUri.value ?: ""
            )
            repository.insertUser(userToSave)
        }
    }

    fun clearData() {
        viewModelScope.launch {
            repository.clearAllData()
            _user.value = null
            _nickname.value = ""
            _id.value = ""
            _avatarUri.value = null
        }
    }
}