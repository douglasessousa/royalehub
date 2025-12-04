package com.douglasessousa.royalehub.ui.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.data.model.Card
import com.douglasessousa.royalehub.data.model.Tower
import com.douglasessousa.royalehub.ui.components.CardView
import com.douglasessousa.royalehub.ui.components.TowerView

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

    var itemToShowInDialog by remember { mutableStateOf<Any?>(null) }

    val canSave = deckName.isNotBlank() && selectedCards.size == 8 && selectedTower != null

    if (itemToShowInDialog != null) {
        ItemInfoDialog(
            item = itemToShowInDialog!!,
            isCardInDeck = { selectedCards.contains(it) },
            isTowerSelected = { selectedTower == it },
            onDismiss = { itemToShowInDialog = null },
            onConfirm = {
                when (val item = itemToShowInDialog) {
                    is Card -> viewModel.toggleCardSelection(item)
                    is Tower -> viewModel.toggleTowerSelection(item)
                }
            }
        )
    }

    Scaffold(
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
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedTextField(
                        value = deckName,
                        onValueChange = { viewModel.updateDeckName(it) },
                        label = { Text("Nome do Deck (Ex: Log Bait)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(16.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) { Text(text = "Cartas Selecionadas", style = MaterialTheme.typography.titleMedium) }
                item(span = { GridItemSpan(maxLineSpan) }) { Spacer(modifier = Modifier.height(8.dp)) }

                item(span = { GridItemSpan(maxLineSpan) }) { SelectedCardsRow(selectedCards) { card -> itemToShowInDialog = card } }
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
                    item(span = { GridItemSpan(maxLineSpan) }) { Text(text = "Escolha sua Torre", style = MaterialTheme.typography.titleMedium) }
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

                    item(span = { GridItemSpan(maxLineSpan) }) { Text(text = "Escolha suas Cartas", style = MaterialTheme.typography.titleMedium) }
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, disabledContainerColor = Color.LightGray)
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
        // Add 3 invisible spacers to push the TowerSlot to the 4th position
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

@Composable
fun CardItem(card: Card, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 93.dp, height = 137.dp)
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

@Composable
fun TowerItem(tower: Tower, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 93.dp, height = 137.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                else Modifier
            )
    ) {
        TowerView(
            tower = tower, 
            onClick = { onClick() }
        )
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

@Composable
private fun ItemInfoDialog(
    item: Any,
    isCardInDeck: (Card) -> Boolean,
    isTowerSelected: (Tower) -> Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val title: String
    val details: String
    val isItemInDeck: Boolean

    when (item) {
        is Card -> {
            title = item.name
            details = "Elixir: ${item.elixir}\nRaridade: ${item.rarity}"
            isItemInDeck = isCardInDeck(item)
        }
        is Tower -> {
            title = item.name
            details = "Raridade: ${item.rarity}"
            isItemInDeck = isTowerSelected(item)
        }
        else -> return
    }

    val buttonText = if (isItemInDeck) "Remover do Deck" else "Adicionar ao Deck"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = details) },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(buttonText)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Fechar") }
        }
    )
}
