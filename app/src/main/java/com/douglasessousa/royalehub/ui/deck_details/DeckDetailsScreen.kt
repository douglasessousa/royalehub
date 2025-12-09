package com.douglasessousa.royalehub.ui.deck_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.ui.deck_details.components.AddMatchDialog
import com.douglasessousa.royalehub.ui.deck_details.components.MatchItem
import com.douglasessousa.royalehub.ui.deck_details.components.StatsCard

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
        containerColor = Color.Transparent,
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
                    Text(
                        "Últimas Partidas",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.surface
                    )

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
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(matches) { match ->
                        MatchItem(match) { viewModel.deleteMatch(match) }
                    }
                }
            }
        }
    }

    if (showAddMatchDialog) {
        AddMatchDialog (
            onDismiss = { showAddMatchDialog = false },
            onConfirm = { isWin ->
                viewModel.addMatch(isWin)
                showAddMatchDialog = false
            }
        )
    }
}