package com.douglasessousa.royalehub.ui.deck

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.ui.deck.components.CardItem
import com.douglasessousa.royalehub.ui.components.CardView
import com.douglasessousa.royalehub.ui.components.TowerView
import com.douglasessousa.royalehub.ui.deck.components.ItemInfoDialog
import com.douglasessousa.royalehub.ui.deck.components.TowerItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    viewModel: DeckViewModel,
    onBack: () -> Unit
) {
    val availableCards by viewModel.availableCards.collectAsState()
    val selectedCards by viewModel.selectedCards.collectAsState()
    val availableTowers by viewModel.availableTowers.collectAsState()
    val selectedTower by viewModel.selectedTower.collectAsState()
    val deckName by viewModel.deckName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val saveError by viewModel.saveError.collectAsState()

    var itemToShowInDialog by remember { mutableStateOf<Any?>(null) }
    var cardForSwap by remember { mutableStateOf<Card?>(null) }
    val gridState = rememberLazyGridState()

    val canSave = deckName.isNotBlank() && selectedCards.size == 8 && selectedTower != null

    // Efeito p/ escrolar para o topo após fazer a troca da carta
    LaunchedEffect(cardForSwap) {
        if (cardForSwap != null) {
            gridState.animateScrollToItem(index = 0)
        }
    }

    if (saveError != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearSaveError() },
            title = { Text("Erro ao Salvar") },
            text = { Text(saveError!!) },
            confirmButton = {
                Button(onClick = { viewModel.clearSaveError() }) {
                    Text("OK")
                }
            }
        )
    }

    if (itemToShowInDialog != null) {
        ItemInfoDialog(
            item = itemToShowInDialog!!,
            isCardInDeck = { selectedCards.contains(it) },
            isDeckFull = selectedCards.size == 8,
            isTowerSelected = { selectedTower == it },
            isAnyTowerSelected = selectedTower != null,
            onDismiss = { itemToShowInDialog = null },
            onConfirm = {
                when (val item = itemToShowInDialog) {
                    is Card -> {
                        if (selectedCards.size >= 8 && !selectedCards.contains(item)) {
                            cardForSwap = item
                        } else {
                            viewModel.toggleCardSelection(item)
                        }
                    }
                    is Tower -> viewModel.toggleTowerSelection(item)
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Novo Deck", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.onPrimary)
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
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedTextField(
                        value = deckName,
                        onValueChange = { viewModel.updateDeckName(it) },
                        label = { Text("Nome do Deck") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {
                        AnimatedVisibility(visible = cardForSwap != null) {
                            Text(
                                text = "Selecione uma carta do seu deck para substituir por ${cardForSwap?.name}.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        Text(
                            text = "Cartas Selecionadas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(8.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    SelectedCardsRow(selectedCards) { clickedCard ->
                        if (cardForSwap != null) {
                            viewModel.swapCard(clickedCard, cardForSwap!!)
                            cardForSwap = null // End swap mode
                        } else {
                            itemToShowInDialog = clickedCard
                        }
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) { SelectedTowerRow(selectedTower) { tower -> itemToShowInDialog = tower } }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) { HorizontalDivider() }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(8.dp)) }

                if (isLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Escolha sua Torre",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(8.dp)) }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(availableTowers) { tower ->
                                val isSelected = selectedTower == tower
                                TowerItem(tower = tower, isSelected = isSelected) {
                                    itemToShowInDialog = tower
                                }
                            }
                        }
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Escolha suas Cartas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(8.dp)) }

                    items(availableCards) { card ->
                        val isSelected = selectedCards.contains(card)
                        CardItem(card = card, isSelected = isSelected) {
                            itemToShowInDialog = card
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
                    disabledContainerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Salvar Deck", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SelectedCardsRow(selectedCards: List<Card>, onCardClick: (Card) -> Unit) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 0..3) {
                CardSlot(card = selectedCards.getOrNull(i), onClick = { c -> c?.let(onCardClick) })
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (i in 4..7) {
                CardSlot(card = selectedCards.getOrNull(i), onClick = { c -> c?.let(onCardClick) })
            }
        }
    }
}

@Composable
fun SelectedTowerRow(selectedTower: Tower?, onTowerClick: (Tower) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Adicionar 3 edspaços invisíveis para a torre ficar na 4ª posição
        Spacer(Modifier.width(75.dp))
        Spacer(Modifier.width(75.dp))
        Spacer(Modifier.width(75.dp))
        TowerSlot(tower = selectedTower, onClick = { t -> t?.let(onTowerClick) })
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
fun TowerSlot(tower: Tower?, onClick: (Tower?) -> Unit) {
    TowerView(
        tower = tower,
        onClick = { onClick(tower) },
        modifier = Modifier
            .width(75.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}