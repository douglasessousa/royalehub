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

    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri = _avatarUri.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUser().first()?.let {
                _user.value = it
                _nickname.value = it.nickname
                _id.value = it.id
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
            val userToSave = User(id = _id.value, nickname = _nickname.value, avatarUrl = _avatarUri.value ?: "")
            repository.insertUser(userToSave)
        }
    }

    fun clearData() {
        viewModelScope.launch {
            repository.clearAllData()
            // Limpa o estado no ViewModel para a UI ser atualizada
            _user.value = null
            _nickname.value = ""
            _id.value = ""
            _avatarUri.value = null
        }
    }
}