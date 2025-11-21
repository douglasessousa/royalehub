package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.douglasessousa.royalehub.data.model.Carta
import com.douglasessousa.royalehub.ui.components.CartaCard
import com.douglasessousa.royalehub.viewmodel.AppViewModel

@Composable
fun CartasScreen(appViewModel: AppViewModel) {
    val cards by appViewModel.cards.collectAsState()
    var cartaSelecionada by remember { mutableStateOf<Carta?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(cards) { carta ->
            CartaCard(
                carta = carta,
                onClick = { cartaSelecionada = carta },
                modifier = Modifier.padding(4.dp)
            )
        }
    }

    cartaSelecionada?.let { carta ->
        AlertDialog(
            onDismissRequest = { cartaSelecionada = null },
            title = { Text(text = carta.nome) },
            text = {
                Text(
                    text = "Elixir: ${carta.elixir}\nRaridade: ${carta.raridade}"
                )
            },
            confirmButton = {
                Button(onClick = { cartaSelecionada = null }) {
                    Text("Fechar")
                }
            }
        )
    }
}
