package com.douglasessousa.royalehub.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.TextGray
import com.douglasessousa.royalehub.ui.theme.WinGreen
import com.douglasessousa.royalehub.ui.theme.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCreateDeck: () -> Unit,
    onNavigateToDeckDetails: (Int) -> Unit,
) {
    val decksUiState by viewModel.decksState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "RoyaleHub",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateDeck,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar Deck")
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (decksUiState.isEmpty()) {
                EmptyState(onNavigateToCreateDeck)
            } else {
                DeckList(decksState = decksUiState, onDeckClick = onNavigateToDeckDetails)
            }
        }
    }
}

@Composable
fun EmptyState(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nenhum deck criado ainda",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Comece criando seu primeiro deck para registrar suas partidas.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Criar Primeiro Deck")
        }
    }
}

@Composable
fun DeckList(decksState: List<DeckUiState>, onDeckClick: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(decksState) { item ->
            DeckItem(item, onDeckClick)
        }
    }
}

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
        shape = RoundedCornerShape(16.dp) // Bordas mais arredondadas
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
                    // Indicador de Elixir Médio
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
                            color = Purple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Badge de Win Rate
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
                // Mostra as 8 cartas
                deck.cards.take(8).forEach { card ->
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .aspectRatio(0.8f)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        AsyncImage(
                            model = card.imageUrl,
                            contentDescription = card.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}