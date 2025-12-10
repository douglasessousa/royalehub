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

/**
 * Painel de Controle de um deck específico.
 * Aqui o usuário pode analisar a performance (Win Rate), ver o histórico
 * e registrar novas batalhas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailsScreen(
    deckId: Int, // O ID do deck q foi clicado na Home
    viewModel: DeckDetailsViewModel,
    onBack: () -> Unit // Ação pra voltar à tela anterior
) {
    // Usa o ID recebido pra mandar o ViewModel buscar os dados no banco de dados.
    LaunchedEffect(deckId) {
        viewModel.loadDeck(deckId)
    }

    val deck by viewModel.deck.collectAsState()
    val matches by viewModel.matches.collectAsState() // Lista de histórico
    val stats by viewModel.stats.collectAsState() // Win Rate calculado

    // Estado pra controlar a visibilidade do Popup de registrar partida
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
                    // Botão de Excluir Deck
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
        // Enquanto o deck n carrega do banco, mostramos um spinner.
        if (deck == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {

                // Gráfico de Estatísticas
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

                // Lista de Histórico
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
                viewModel.addMatch(isWin) // Avisa o ViewModel
                showAddMatchDialog = false
            }
        )
    }
}