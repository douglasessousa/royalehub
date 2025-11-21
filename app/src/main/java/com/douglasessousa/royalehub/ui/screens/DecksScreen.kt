package com.douglasessousa.royalehub.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.douglasessousa.royalehub.data.model.Deck
import com.douglasessousa.royalehub.navigation.Routes
import com.douglasessousa.royalehub.ui.components.DeckCard
import com.douglasessousa.royalehub.viewmodel.AppViewModel

const val MAX_DECKS = 8

@Composable
fun DecksScreen(appViewModel: AppViewModel, navController: NavController) {
    val decks by appViewModel.decks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Meus Decks",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(decks) { _, deck ->
                DeckCard(deck = deck, onClick = {
                    navController.navigate(Routes.DeckDetail.createRoute(deck.id))
                })
            }

            val emptySlots = MAX_DECKS - decks.size
            if (emptySlots > 0) {
                items(count = emptySlots) { 
                    CreateDeckCard {
                        // Navega para a criação de um novo deck (id -1, por exemplo)
                        navController.navigate(Routes.DeckDetail.createRoute(-1))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateDeckCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(2.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = "Criar novo deck", tint = Color.Gray)
            Text(text = "Criar Deck", color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}
