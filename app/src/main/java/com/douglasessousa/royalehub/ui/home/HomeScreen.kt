package com.douglasessousa.royalehub.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.ui.home.components.DeckItem
import com.douglasessousa.royalehub.ui.home.components.EmptyState

/**
 * TELA INICIAL (Home)
 *
 * Esta é a tela principal onde o usuário vê seus decks criados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCreateDeck: () -> Unit,
    onNavigateToDeckDetails: (Int) -> Unit,
) {
    val decksUiState by viewModel.decksState.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
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
        // Botão Flutuante (+) no canto inferior direito
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
                // Se não houver decks, mostra a tela de "Comece aqui"
                EmptyState(onNavigateToCreateDeck)
            } else {
                // Se houver decks, mostra a lista
                DeckList(decksState = decksUiState, onDeckClick = onNavigateToDeckDetails)
            }
        }
    }
}

/**
 * Lista de Decks
 */
@Composable
fun DeckList(decksState: List<DeckUiState>, onDeckClick: (Int) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Itera sobre a lista de decks e cria um Card para cada um
        items(decksState) { item ->
            DeckItem(item, onDeckClick)
        }
    }
}