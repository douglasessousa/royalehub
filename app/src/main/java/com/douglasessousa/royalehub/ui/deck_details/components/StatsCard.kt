package com.douglasessousa.royalehub.ui.deck_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasessousa.royalehub.ui.deck_details.DeckStats
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.TextGray
import com.douglasessousa.royalehub.ui.theme.WinGreen

/**
 * O Gráfico de Estatísticas
 *
 * Este componente desenha o painel principal da tela de detalhes.
 * Ele exibe o Win Rate de forma gráfica e os contadores absolutos.
 */
@Composable
fun StatsCard(stats: DeckStats) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Resumo",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(120.dp),
                    color = LossRed.copy(alpha = 0.3f),
                    strokeWidth = 12.dp,
                )
                CircularProgressIndicator(
                    progress = { stats.winRate },
                    modifier = Modifier.size(120.dp),
                    color = WinGreen,
                    strokeWidth = 12.dp,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(stats.winRate * 100).toInt()}%",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Win Rate", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(count = stats.wins.toString(), label = "Vitórias", color = WinGreen)
                StatItem(count = stats.losses.toString(), label = "Derrotas", color = LossRed)
                StatItem(count = stats.total.toString(), label = "Total", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

/**
 * ITEM DE ESTATÍSTICA
 * Pequeno componente reutilizável pra mostrar um número colorido com uma legenda em baixo.
 */
@Composable
fun StatItem(count: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextGray)
    }
}