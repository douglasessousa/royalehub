package com.douglasessousa.royalehub.ui.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.ui.components.CardView
import com.douglasessousa.royalehub.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    viewModel: DeckViewModel,
    onBack: () -> Unit
) {
    val availableCards by viewModel.availableCards.collectAsState()
    val selectedCards by viewModel.selectedCards.collectAsState()
    val deckName by viewModel.deckName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val canSave = deckName.isNotBlank() && selectedCards.size == 8

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Novo Deck",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = deckName,
                onValueChange = { viewModel.updateDeckName(it) },
                label = { Text("Nome do Deck (Ex: Log Bait)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cartas Selecionadas (${selectedCards.size}/8)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            SelectedCardsRow(selectedCards) { card ->
                viewModel.toggleCardSelection(card)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 75.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(availableCards) { card ->
                        val isSelected = selectedCards.contains(card)
                        CardItem(card = card, isSelected = isSelected) {
                            viewModel.toggleCardSelection(card)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveDeck(onSuccess = onBack) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Salvar Deck", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SelectedCardsRow(selectedCards: List<Card>, onRemove: (Card) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0..3) {
                CardSlot(card = selectedCards.getOrNull(i), onClick = { c -> c?.let { onRemove(it) } })
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 4..7) {
                CardSlot(card = selectedCards.getOrNull(i), onClick = { c -> c?.let { onRemove(it) } })
            }
        }
    }
}

@Composable
fun CardSlot(card: Card?, onClick: (Card?) -> Unit) {
    CardView(
        card = card,
        onClick = { onClick(card) },
        modifier = Modifier
            .width(75.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun CardItem(card: Card, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                else Modifier
            )
    ) {
        CardView(card = card, onClick = { onClick() })
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
            }
        }
    }
}
