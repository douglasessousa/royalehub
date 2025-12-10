package com.douglasessousa.royalehub.ui.deck_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.BorderStroke

/**
 * BOTÃO DE RESULTADO
 *
 * Este componente cria um botão quadrado grande, usado dentro do modal de registro.
 * Ele foi desenhado para ser flexível: a cor, o ícone e o texto são passados
 * como parâmetros, permitindo que o mesmo código desenhe tanto o botão
 * de "Vitória" (Verde) quanto o de "Derrota" (Vermelho).
 *
 * @param text O texto a ser exibido (ex: "Vitória").
 * @param icon O ícone vetorial.
 * @param color A cor base do botão (afeta o fundo, borda e texto).
 * @param onClick A função a ser executada quando o usuário toca no botão.
 */
@Composable
fun ResultButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(2.dp, color.copy(alpha = 0.5f)),
        modifier = Modifier.size(110.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}