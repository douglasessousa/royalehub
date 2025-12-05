package com.douglasessousa.royalehub.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.ui.home.DeckUiState
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.Purple
import com.douglasessousa.royalehub.ui.theme.WinGreen

@Composable
fun DeckItem(item: DeckUiState, onClick: (Int) -> Unit) {
    val deck = item.deck

    val badgeColor = when {
        item.totalMatches == 0 -> Color.LightGray
        item.winRate >= 0.5 -> WinGreen
        else -> LossRed
    }

    Card(
        onClick = { onClick(deck.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = deck.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Purple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%.1f Médio".format(item.averageElixir),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF9C27B0),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Surface(
                    color = badgeColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "${(item.winRate * 100).toInt()}% WR",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Renderiza as 8 cartas
                deck.cards.forEach { card ->
                    MiniCardImage(imageUrl = card.imageUrl, contentDesc = card.name)
                }

                // Renderiza a Torre
                if (deck.tower != null) {
                    MiniCardImage(
                        imageUrl = deck.tower.imageUrl,
                        contentDesc = deck.tower.name
                    )
                }
            }
        }
    }
}

@Composable
fun MiniCardImage(imageUrl: String, contentDesc: String) {
    Box(
        modifier = Modifier
            .width(32.dp)
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDesc,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}