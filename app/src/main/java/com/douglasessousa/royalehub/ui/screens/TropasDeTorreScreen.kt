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
import com.douglasessousa.royalehub.data.model.TropaDeTorre
import com.douglasessousa.royalehub.ui.components.TropaDeTorreCard
import com.douglasessousa.royalehub.viewmodel.AppViewModel

@Composable
fun TropasDeTorreScreen(appViewModel: AppViewModel) {
    val towers by appViewModel.towers.collectAsState()
    var tropaSelecionada by remember { mutableStateOf<TropaDeTorre?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(towers) { tower ->
            TropaDeTorreCard(
                tropaDeTorre = tower,
                onClick = { tropaSelecionada = tower },
                modifier = Modifier.padding(4.dp)
            )
        }
    }

    tropaSelecionada?.let { tropa ->
        AlertDialog(
            onDismissRequest = { tropaSelecionada = null },
            title = { Text(text = tropa.nome) },
            text = { Text(text = "Raridade: ${tropa.raridade}") },
            confirmButton = {
                Button(onClick = { tropaSelecionada = null }) {
                    Text("Fechar")
                }
            }
        )
    }
}
