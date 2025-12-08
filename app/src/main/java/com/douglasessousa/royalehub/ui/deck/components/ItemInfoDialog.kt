package com.douglasessousa.royalehub.ui.deck.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.ui.theme.LossRed

@Composable
fun ItemInfoDialog(
    item: Any,
    isCardInDeck: (Card) -> Boolean,
    isDeckFull: Boolean,
    isTowerSelected: (Tower) -> Boolean,
    isAnyTowerSelected: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val title: String
    val imageUrl: String
    val elixir: Int?
    val rarity: String
    val buttonText: String
    val isDestructiveAction: Boolean

    when (item) {
        is Card -> {
            title = item.name
            imageUrl = item.imageUrl
            elixir = item.elixir
            rarity = item.rarity
            val isThisCardInDeck = isCardInDeck(item)
            buttonText = if (isThisCardInDeck) "Remover do Deck" else if (isDeckFull) "Trocar Carta" else "Adicionar ao Deck"
            isDestructiveAction = isThisCardInDeck
        }
        is Tower -> {
            title = item.name
            imageUrl = item.imageUrl
            elixir = null
            rarity = item.rarity
            val isThisTowerSelected = isTowerSelected(item)
            buttonText = when {
                isThisTowerSelected -> "Remover Torre"
                isAnyTowerSelected -> "Trocar Torre"
                else -> "Selecionar Torre"
            }
            isDestructiveAction = isThisTowerSelected
        }
        else -> return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = null,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Título
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagem
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Elixir
                    if (elixir != null) {
                        AttributeBadge(
                            icon = Icons.Default.WaterDrop,
                            iconTint = Color(0xFF9C27B0), // Roxo Elixir
                            text = "Elixir: $elixir"
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }

                    // Raridade
                    val rarityColor = getRarityColor(rarity)
                    AttributeBadge(
                        icon = Icons.Default.Star,
                        iconTint = rarityColor,
                        text = rarity.replaceFirstChar { it.uppercase() },
                        textColor = rarityColor
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    // se for remover, botão vermelho
                    // se for adicionar, botão primário
                    containerColor = if (isDestructiveAction) LossRed else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText)
            }
        }
    )
}


fun getRarityColor(rarity: String): Color {
    return when (rarity.lowercase()) {
        "comum", "common" -> Color(0xFF42A5F5) // Azul
        "rara", "rare" -> Color(0xFFFFA726)   // Laranja
        "épica", "epic" -> Color(0xFFAB47BC)  // Roxo
        "lendária", "legendary" -> Color(0xFF26C6DA) // Ciano
        "campeão", "champion" -> Color(0xFFFFD700) // Dourado
        else -> Color.Gray
    }
}