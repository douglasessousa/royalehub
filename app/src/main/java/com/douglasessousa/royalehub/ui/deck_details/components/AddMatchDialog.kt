package com.douglasessousa.royalehub.ui.deck_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.TextGray
import com.douglasessousa.royalehub.ui.theme.WinGreen

/**
 * Este componente é um "Modal" (janela flutuante) personalizado.
 *
 * @param onDismiss Função chamada pra fechar o diálogo (clicar fora ou cancelar).
 * @param onConfirm Função q devolve o resultado: 'true' (Vitória) ou 'false' (Derrota).
 */
@Composable
fun AddMatchDialog(onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, // Fecha se o usuário clicar fora da caixa
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Registrar Partida",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Qual foi o resultado da batalha?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botão de Vitória
                    ResultButton(
                        text = "Vitória",
                        icon = Icons.Default.EmojiEvents,
                        color = WinGreen,
                        onClick = { onConfirm(true) }
                    )

                    // Botão de Derrota
                    ResultButton(
                        text = "Derrota",
                        icon = Icons.Default.SentimentVeryDissatisfied,
                        color = LossRed,
                        onClick = { onConfirm(false) }
                    )
                }
            }
        },
        // Botões padrões vazios, já q foi criado os próprios.
        confirmButton = {},
        dismissButton = {}
    )
}