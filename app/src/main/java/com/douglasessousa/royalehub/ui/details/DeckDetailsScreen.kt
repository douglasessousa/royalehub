package com.douglasessousa.royalehub.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.douglasessousa.royalehub.data.model.MatchResult
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.TextGray
import com.douglasessousa.royalehub.ui.theme.WinGreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailsScreen(
    deckId: Int,
    viewModel: DeckDetailsViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(deckId) {
        viewModel.loadDeck(deckId)
    }

    val deck by viewModel.deck.collectAsState()
    val matches by viewModel.matches.collectAsState()
    val stats by viewModel.stats.collectAsState()

    var showAddMatchDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = deck?.name ?: "Detalhes",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteDeck(onDeleted = onBack) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Excluir Deck",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        if (deck == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {

                StatsCard(stats)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Últimas Partidas", style = MaterialTheme.typography.titleLarge)

                    Button(
                        onClick = { showAddMatchDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Registrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(matches) { match ->
                        MatchItem(match) { viewModel.deleteMatch(match) }
                    }
                }
            }
        }
    }

    if (showAddMatchDialog) {
        AddMatchDialog(
            onDismiss = { showAddMatchDialog = false },
            onConfirm = { isWin ->
                viewModel.addMatch(isWin)
                showAddMatchDialog = false
            }
        )
    }
}

@Composable
fun StatsCard(stats: DeckStats) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background), // Branco
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

@Composable
fun StatItem(count: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextGray)
    }
}

@Composable
fun MatchItem(match: MatchResult, onDelete: () -> Unit) {
    val color = if (match.isWin) WinGreen else LossRed
    val icon = if (match.isWin) Icons.Default.EmojiEvents else Icons.Default.SentimentVeryDissatisfied
    val text = if (match.isWin) "Vitória" else "Derrota"

    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'às' HH:mm", Locale("pt", "BR"))

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = color)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = text, fontWeight = FontWeight.Bold, color = color)
                    Text(
                        text = dateFormat.format(Date(match.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = TextGray)
            }
        }
    }
}

@Composable
fun AddMatchDialog(onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
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
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun ResultButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, color.copy(alpha = 0.5f)),
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