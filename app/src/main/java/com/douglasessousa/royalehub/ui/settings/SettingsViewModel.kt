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

/**
 * ViewModel para a [SettingsScreen].
 *
 * Responsável pela lógica de negócios relacionada às configurações do usuário. Isso inclui:
 * - Carregar os dados do perfil do usuário do banco de dados local.
 * - Manter o estado dos campos de edição (nickname, ID do jogador, avatar).
 * - Salvar as alterações do perfil de volta no banco de dados.
 * - Lidar com a lógica de limpeza de todos os dados do aplicativo.
 *
 * A arquitetura do usuário é projetada para haver apenas um único registro de usuário no banco de dados,
 * identificado por uma chave primária fixa (pk = 0), garantindo que as operações de salvamento
 * sempre atualizem o mesmo perfil em vez de criar novos.
 *
 * @property repository O repositório que abstrai o acesso aos dados.
 */
class SettingsViewModel(private val repository: RoyaleRepository) : ViewModel() {

    // Estados privados mutáveis, acessíveis apenas dentro do ViewModel.
    private val _user = MutableStateFlow<User?>(null)
    private val _nickname = MutableStateFlow("")
    private val _id = MutableStateFlow("") // Representa o gameId do usuário
    private val _avatarUri = MutableStateFlow<String?>(null)

    // Estados públicos imutáveis, expostos para a UI observar.
    /** O apelido (nickname) do usuário. */
    val nickname = _nickname.asStateFlow()
    /** O ID do jogador. */
    val id = _id.asStateFlow()
    /** A URI da imagem de avatar do usuário, como uma String. */
    val avatarUri = _avatarUri.asStateFlow()

    /**
     * No momento da inicialização do ViewModel, carrega o perfil de usuário existente (se houver)
     * do banco de dados e preenche os estados da UI.
     */
    init {
        viewModelScope.launch {
            repository.getUser().first()?.let {
                _user.value = it
                _nickname.value = it.nickname
                _id.value = it.gameId // Carrega o gameId, não a chave primária
                if (it.avatarUrl.isNotEmpty()) {
                    _avatarUri.value = it.avatarUrl
                }
            }
        }
    }

    /** Atualiza o estado do nickname conforme o usuário digita. */
    fun onNicknameChange(nickname: String) {
        _nickname.value = nickname
    }

    /** Atualiza o estado do ID do jogador conforme o usuário digita. */
    fun onIdChange(id: String) {
        _id.value = id
    }

    /** Atualiza o estado do avatar quando uma nova imagem é selecionada. */
    fun onAvatarChange(uri: Uri?) {
        _avatarUri.value = uri?.toString()
    }

    /**
     * Salva o perfil do usuário no banco de dados.
     * Cria um objeto [User] com a chave primária fixa (pk = 0) para garantir que
     * a operação de inserção sempre atualize o registro existente (devido ao OnConflictStrategy.REPLACE).
     */
    fun saveUser() {
        viewModelScope.launch {
            val userToSave = User(
                pk = 0, // Chave primária fixa para garantir um único registro de usuário.
                gameId = _id.value,
                nickname = _nickname.value,
                avatarUrl = _avatarUri.value ?: ""
            )
            repository.insertUser(userToSave)
        }
    }

    /**
     * Executa a limpeza de todos os dados do aplicativo e redefine os estados da UI.
     */
    fun clearData() {
        viewModelScope.launch {
            repository.clearAllData()
            // Reseta os estados na UI para refletir a limpeza dos dados.
            _user.value = null
            _nickname.value = ""
            _id.value = ""
            _avatarUri.value = null
        }
    }
}
