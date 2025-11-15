package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.ui.components.CartaCard
import com.douglasessousa.royalehub.ui.components.TropaDeTorreCard
import com.douglasessousa.royalehub.viewmodel.AppViewModel

@Composable
fun DeckDetailScreen(appViewModel: AppViewModel, deckId: Int) {
    val decks by appViewModel.decks.collectAsState()
    val deck = if (deckId == -1) {
        Deck(nome = "Novo Deck", cartas = emptyList(), tropaDeTorre = null)
    } else {
        decks.find { it.id == deckId }
    }

    if (deck == null) {
        Text(text = "Deck não encontrado")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = deck.nome, style = MaterialTheme.typography.headlineLarge)

        // Fileiras de Cartas
        (0..1).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                (0..3).forEach { col ->
                    val index = row * 4 + col
                    CartaCard(
                        carta = deck.cartas.getOrNull(index),
                        onClick = { /* Ação futura */ },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Fileira da Tropa de Torre
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Usamos um Box com weight em uma Row com 3 itens para centralizar o do meio
            Box(modifier = Modifier.weight(1.5f)) // Espaçador
            TropaDeTorreCard(
                tropaDeTorre = deck.tropaDeTorre,
                onClick = { /* Ação futura */ },
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1.5f)) // Espaçador
        }
    }
}
