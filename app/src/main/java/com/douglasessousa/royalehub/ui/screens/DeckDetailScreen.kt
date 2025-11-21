package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import com.douglasessousa.royalehub.ui.components.CartaCard
import com.douglasessousa.royalehub.ui.components.TropaDeTorreCard
import com.douglasessousa.royalehub.viewmodel.AppViewModel

const val TOWER_SLOT_INDEX = 99
enum class LibrarySelection { NONE, CARD, TOWER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(appViewModel: AppViewModel, deckId: Int, navController: NavController) {
    val originalDeck by appViewModel.decks.collectAsState().value.find { it.id == deckId }.let { rememberUpdatedState(it) }

    var deckName by remember { mutableStateOf("") }
    var carta1 by remember { mutableStateOf<Carta?>(null) }
    var carta2 by remember { mutableStateOf<Carta?>(null) }
    var carta3 by remember { mutableStateOf<Carta?>(null) }
    var carta4 by remember { mutableStateOf<Carta?>(null) }
    var carta5 by remember { mutableStateOf<Carta?>(null) }
    var carta6 by remember { mutableStateOf<Carta?>(null) }
    var carta7 by remember { mutableStateOf<Carta?>(null) }
    var carta8 by remember { mutableStateOf<Carta?>(null) }
    var tropaDeTorreSlot by remember { mutableStateOf<TropaDeTorre?>(null) }

    val cardSlots = listOf(carta1, carta2, carta3, carta4, carta5, carta6, carta7, carta8)
    val isDeckComplete = cardSlots.all { it != null } && tropaDeTorreSlot != null

    var selectedSlotIndex by remember { mutableStateOf<Int?>(null) }
    var librarySelection by remember { mutableStateOf(LibrarySelection.NONE) }
    var itemParaExibir by remember { mutableStateOf<Any?>(null) }

    LaunchedEffect(originalDeck) {
        originalDeck?.let {
            deckName = it.nome
            carta1 = it.cartas.getOrNull(0)
            carta2 = it.cartas.getOrNull(1)
            carta3 = it.cartas.getOrNull(2)
            carta4 = it.cartas.getOrNull(3)
            carta5 = it.cartas.getOrNull(4)
            carta6 = it.cartas.getOrNull(5)
            carta7 = it.cartas.getOrNull(6)
            carta8 = it.cartas.getOrNull(7)
            tropaDeTorreSlot = it.tropaDeTorre
        } ?: run {
            deckName = "Novo Deck"
        }
    }

    fun onDialogConfirm(item: Any, isItemInDeck: Boolean) {
        if (isItemInDeck) { // --- AÇÃO DE REMOVER ---
            if (item is Carta) {
                if (carta1?.id == item.id) carta1 = null
                if (carta2?.id == item.id) carta2 = null
                if (carta3?.id == item.id) carta3 = null
                if (carta4?.id == item.id) carta4 = null
                if (carta5?.id == item.id) carta5 = null
                if (carta6?.id == item.id) carta6 = null
                if (carta7?.id == item.id) carta7 = null
                if (carta8?.id == item.id) carta8 = null
            }
            if (item is TropaDeTorre && tropaDeTorreSlot?.id == item.id) {
                tropaDeTorreSlot = null
            }
        } else { // --- AÇÃO DE ADICIONAR/TROCAR ---
            if (item is Carta) {
                if (cardSlots.any { it?.id == item.id }) return
                when (selectedSlotIndex) {
                    0 -> carta1 = item
                    1 -> carta2 = item
                    2 -> carta3 = item
                    3 -> carta4 = item
                    4 -> carta5 = item
                    5 -> carta6 = item
                    6 -> carta7 = item
                    7 -> carta8 = item
                }
            }
            if (item is TropaDeTorre) {
                tropaDeTorreSlot = item
            }
        }
        itemParaExibir = null
        librarySelection = LibrarySelection.NONE
    }

    if (itemParaExibir != null) {
        val isItemInDeck = when (val item = itemParaExibir) {
            is Carta -> cardSlots.any { it?.id == item.id }
            is TropaDeTorre -> tropaDeTorreSlot?.id == item.id
            else -> false
        }
        ItemInfoDialog(
            item = itemParaExibir!!,
            isItemInDeck = isItemInDeck,
            onDismiss = { itemParaExibir = null },
            onConfirm = { onDialogConfirm(itemParaExibir!!, isItemInDeck) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Deck") },
                actions = {
                    if (originalDeck != null) {
                        IconButton(onClick = {
                            appViewModel.deleteDeck(originalDeck!!)
                            navController.navigateUp()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Apagar Deck")
                        }
                    }
                    IconButton(
                        onClick = {
                            val deckFinal = Deck(
                                id = originalDeck?.id ?: 0,
                                nome = deckName,
                                cartas = cardSlots.filterNotNull(),
                                tropaDeTorre = tropaDeTorreSlot
                            )
                            appViewModel.upsertDeck(deckFinal)
                            navController.navigateUp()
                        },
                        enabled = isDeckComplete // <<< ALTERAÇÃO APLICADA AQUI
                    ) {
                        Icon(imageVector = Icons.Default.Done, contentDescription = "Salvar Deck")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = deckName,
                onValueChange = { deckName = it },
                label = { Text("Nome do Deck") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    CartaCard(carta = carta1, onClick = { selectedSlotIndex = 0; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta2, onClick = { selectedSlotIndex = 1; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta3, onClick = { selectedSlotIndex = 2; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta4, onClick = { selectedSlotIndex = 3; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    CartaCard(carta = carta5, onClick = { selectedSlotIndex = 4; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta6, onClick = { selectedSlotIndex = 5; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta7, onClick = { selectedSlotIndex = 6; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                    CartaCard(carta = carta8, onClick = { selectedSlotIndex = 7; librarySelection = LibrarySelection.CARD }, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.weight(1.5f))
                    TropaDeTorreCard(tropaDeTorre = tropaDeTorreSlot, onClick = { selectedSlotIndex = TOWER_SLOT_INDEX; librarySelection = LibrarySelection.TOWER }, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.weight(1.5f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                when (librarySelection) {
                    LibrarySelection.CARD -> CardLibraryGrid(appViewModel) { item -> itemParaExibir = item }
                    LibrarySelection.TOWER -> TowerLibraryGrid(appViewModel) { item -> itemParaExibir = item }
                    LibrarySelection.NONE -> {}
                }
            }
        }
    }
}

@Composable
private fun ItemInfoDialog(item: Any, isItemInDeck: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    val title = if (item is Carta) item.nome else (item as? TropaDeTorre)?.nome ?: ""
    val details = if (item is Carta) "Elixir: ${item.elixir}\nRaridade: ${item.raridade}" else "Raridade: ${(item as? TropaDeTorre)?.raridade ?: ""}"
    val buttonText = if (isItemInDeck) "Remover" else "Adicionar/Trocar"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = details) },
        confirmButton = { Button(onClick = onConfirm) { Text(buttonText) } },
        dismissButton = { Button(onClick = onDismiss) { Text("Fechar") } }
    )
}

@Composable
private fun CardLibraryGrid(appViewModel: AppViewModel, onCardSelected: (Carta) -> Unit) {
    val cards by appViewModel.cards.collectAsState()
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 80.dp)) {
        items(cards) { carta -> CartaCard(carta = carta, onClick = { onCardSelected(carta) }) }
    }
}

@Composable
private fun TowerLibraryGrid(appViewModel: AppViewModel, onTowerSelected: (TropaDeTorre) -> Unit) {
    val towers by appViewModel.towers.collectAsState()
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 80.dp)) {
        items(towers) { tower -> TropaDeTorreCard(tropaDeTorre = tower, onClick = { onTowerSelected(tower) }) }
    }
}
