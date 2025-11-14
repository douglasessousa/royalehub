package com.douglasessousa.royalehub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.data.model.Deck


@Composable
fun DeckCard(deck: Deck) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = deck.nome)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${deck.cartas.size} cartas")
        }
    }
}